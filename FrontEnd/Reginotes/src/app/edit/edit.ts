import { Component, OnInit } from '@angular/core';
import { NoteService } from '../edit/edit.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ChangeDetectorRef } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { RouterModule, ActivatedRoute } from '@angular/router';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-edit',
  standalone: true,
  imports: [FormsModule, CommonModule, RouterModule],
  templateUrl: './edit.html',
  styleUrls: ['./edit.css'],
})
export class Edit implements OnInit {


  markdownContent = '';
  renderedHtml = '';
  title = '';
  errorMessage = '';
  isLoading = false;
  noteId: string | null = null;
  isPreviewMode = false;

  constructor(
    private noteService: NoteService,
    private route: ActivatedRoute,
    private cdr: ChangeDetectorRef,
    private router: Router,
    private http: HttpClient,
  ) {}

   private readonly API_URL = environment.apiUrl;

  ngOnInit(): void {
    this.noteId = this.route.snapshot.paramMap.get('id');
    if (this.noteId) {
      this.fetchNote(this.noteId);
    }
  }
  username = localStorage.getItem('username') ?? 'User';

logout(): void {
  const refreshToken = localStorage.getItem('refreshToken');

  this.http.delete(`${this.API_URL}/auth/logout`, {
    body: { refreshToken }
  }).subscribe({
    next: () => {
      localStorage.removeItem('token');
      localStorage.removeItem('refreshToken');
      this.router.navigate(['/login']);
    },
    error: () => {
      // clear anyway even if request fails
      localStorage.removeItem('token');
      localStorage.removeItem('refreshToken');
      this.router.navigate(['/login']);
    }
  });
}

  fetchNote(id: string): void {
  this.noteService.getNoteById(id).subscribe({
    next: (note) => {
      this.title = note.title;
      this.markdownContent = note.contentMarkdown;
      this.renderedHtml = note.contentHtml;
      this.cdr.detectChanges(); // ← add this
    },
    error: (err) => console.error('Failed to fetch note', err)
  });
}

  save(): void {
    if (!this.markdownContent.trim()) return;
    this.isLoading = true;
    this.errorMessage = '';

    const request$ = this.noteId
      ? this.noteService.updateNote(this.noteId, this.title, this.markdownContent)
      : this.noteService.convertMarkdown(this.title, this.markdownContent);

    request$.subscribe({
      next: (response) => {
        console.log('save response:', response);
        this.isLoading = false;

        if (!this.noteId && response?.id) {
          this.noteId = response.id;
        }
        this.router.navigate(['/preview', this.noteId]);

      },
      error: (err) => {
        this.errorMessage = 'Failed to save note. Please try again.';
        this.isLoading = false;
        console.error(err);
      },
    });
  }
}
