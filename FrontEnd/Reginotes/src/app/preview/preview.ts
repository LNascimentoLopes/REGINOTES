import { Component, OnInit, HostListener } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ChangeDetectorRef } from '@angular/core';
import { environment } from '../../environments/environment';

interface Tag {
  id: string;
  tagName: string;
  tagColor: string;
}

@Component({
  selector: 'app-preview',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './preview.html',
  styleUrls: ['./preview.css'],
})
export class Preview implements OnInit {
  noteId: string | null = null;
  title = '';
  contentHtml = '';
  private readonly API_URL = environment.apiUrl;

  // Tag state
  noteTags: Tag[] = [];
  allTags: Tag[] = [];
  showTagPanel = false;
  newTagName = '';
  newTagColor = '#000000';
  tagError = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient,
    private cdr: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {
    this.noteId = this.route.snapshot.paramMap.get('id');
    if (this.noteId) {
      this.fetchNote(this.noteId);
      this.fetchAllTags();
    }
  }

  username = localStorage.getItem('username') ?? 'User';

  logout(): void {
    const refreshToken = localStorage.getItem('refreshToken');
    this.http
      .delete(`${this.API_URL}/auth/logout`, {
        body: { refreshToken },
      })
      .subscribe({
        next: () => {
          localStorage.removeItem('token');
          localStorage.removeItem('refreshToken');
          this.router.navigate(['/login']);
        },
        error: () => {
          localStorage.removeItem('token');
          localStorage.removeItem('refreshToken');
          this.router.navigate(['/login']);
        },
      });
  }

  fetchNote(id: string): void {
    this.http.get<any>(`${this.API_URL}/notes/${id}`).subscribe({
      next: (note) => {
        this.title = note.title;
        this.contentHtml = note.contentHtml;

        // FIX: Filter out any null or malformed items from the backend
        // so '@for (...; track tag.id)' never encounters a null item.
        this.noteTags = (note.tags ?? []).filter((t: any) => t && t.id);

        this.cdr.detectChanges();
      },
      error: (err) => console.error('Failed to fetch note', err),
    });
  }

  fetchAllTags(): void {
    this.http.get<any>(`${this.API_URL}/notes/tags`).subscribe({
      next: (response) => {
        console.log('RegiNotes Backend Tags:', response);

        // FIX: Extract the array from Spring Data's 'content' property
        if (response && Array.isArray(response.content)) {
          this.allTags = response.content;
        } else if (Array.isArray(response)) {
          this.allTags = response; // Fallback if it ever flips to a raw array
        } else {
          this.allTags = [];
        }

        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Failed to fetch tags', err);
        this.allTags = [];
        this.cdr.detectChanges();
      },
    });
  }

  toggleTagPanel(event: MouseEvent): void {
    event.stopPropagation();
    this.showTagPanel = !this.showTagPanel;
    this.tagError = '';
    this.newTagName = '';

    // FIX: Fetch fresh tags every time we open the panel!
    if (this.showTagPanel) {
      this.fetchAllTags();
    }
  }

  isTagAssigned(tag: any): boolean {
    if (!this.noteTags || !tag) return false;
    // Check by unique ID to see if the tag is currently linked to the note
    return this.noteTags.some((t: any) => t.id === tag.id);
  }

  toggleTag(tag: any): void {
    if (!this.noteId) return;

    const isAssigned = this.isTagAssigned(tag);

    if (isAssigned) {
      this.http.delete(`${this.API_URL}/notes/${this.noteId}/tags/${tag.id}`).subscribe({
        next: () => {
          // Force a new array reference when removing a tag
          this.noteTags = this.noteTags.filter((t: any) => t.id !== tag.id);
          this.cdr.detectChanges();
        },
        error: (err) => console.error('Failed to remove tag', err),
      });
    } else {
      const payload = {
        id: tag.id,
        tagName: tag.tagName,
        tagColor: tag.tagColor,
      };

      this.http.post(`${this.API_URL}/notes/${this.noteId}/tags`, payload).subscribe({
        next: (savedTag: any) => {
          // FIX: Force a brand new array reference so Angular instantly re-renders the DOM
          this.noteTags = [...this.noteTags, tag];

          this.cdr.detectChanges();
        },
        error: (err) => {
          console.error('Failed to assign tag', err);
        },
      });
    }
  }

  createAndAssignTag(): void {
    const tagName = this.newTagName.trim();

    if (!tagName || !this.noteId) {
      this.tagError = 'Tag name cannot be empty.';
      return;
    }
    this.tagError = '';

    this.http
      .post<any>(`${this.API_URL}/notes/${this.noteId}/tags`, {
        tagName,
        tagColor: this.newTagColor,
      })
      .subscribe({
        next: (respostaBackend) => {
          // Log para você descobrir o que o back-end manda de verdade
          console.log('Retorno de criação de tag:', respostaBackend);

          // 👉 A SOLUÇÃO GARANTIDA AQUI:
          // Aciona as buscas de novo. Como o POST já salvou no banco,
          // o GET vai trazer a lista 100% atualizada sem precisar de F5.
          this.fetchAllTags();
          this.fetchNote(this.noteId!);

          // Limpa o formulário
          this.newTagName = '';
          this.newTagColor = '#48514D';
          this.cdr.detectChanges();
        },
        error: () => {
          this.tagError = 'Failed to create tag.';
        },
      });
  }

  removeNoteTag(tag: Tag, event: MouseEvent): void {
    event.stopPropagation();
    if (!this.noteId) return;
    this.http.delete(`${this.API_URL}/notes/${this.noteId}/tags/${tag.id}`).subscribe({
      next: () => {
        this.noteTags = this.noteTags.filter((t) => t.id !== tag.id);
        this.cdr.detectChanges();
      },
      error: () => {
        this.tagError = 'Failed to remove tag.';
      },
    });
  }

  editNote(): void {
    this.router.navigate(['/edit', this.noteId]);
  }

  deleteNote(): void {
    if (!this.noteId) return;
    this.http.delete(`${this.API_URL}/notes/${this.noteId}`).subscribe({
      next: () => this.router.navigate(['/home']),
      error: (err) => console.error('Failed to delete note', err),
    });
  }
  // 1. Initialize track state
  deletingTagId: string | null = null;

  // 2. Open inline confirmation panel
  deleteGlobalTag(tag: any, event: Event): void {
    if (event) {
      event.preventDefault();
      event.stopPropagation(); // Stops row click from toggling tag assignment
    }
    this.deletingTagId = tag.id;
    this.cdr.detectChanges();
  }

  // 3. Confirming API request
  confirmGlobalDelete(tag: any, event: Event): void {
    if (event) {
      event.preventDefault();
      event.stopPropagation(); // Essential: stops parent action triggers
    }

    this.http.delete(`${this.API_URL}/notes/tags/${tag.id}`).subscribe({
      next: () => {
        // Purge deleted tag from arrays using clean reference copies
        this.allTags = (this.allTags ?? []).filter((t: any) => t && t.id !== tag.id);
        this.noteTags = (this.noteTags ?? []).filter((t: any) => t && t.id !== tag.id);

        this.deletingTagId = null; // reset state tracker
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Failed to delete global tag', err);
        this.deletingTagId = null;
        this.cdr.detectChanges();
      },
    });
  }

  // 4. Cancel action handler
  cancelDelete(event: Event): void {
    if (event) {
      event.preventDefault();
      event.stopPropagation(); // Stops template click bubbling
    }
    this.deletingTagId = null;
    this.cdr.detectChanges();
  }

  goBack(): void {
    history.back();
  }
}
