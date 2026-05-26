import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-preview',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './preview.html',
  styleUrls: ['./preview.css'],
})
export class Preview implements OnInit {
  noteId: string | null = null;
  title = '';
  contentHtml = '';

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
    }
  }
  username = localStorage.getItem('username') ?? 'User';

logout(): void {
  const refreshToken = localStorage.getItem('refreshToken');

  this.http.delete('http://localhost:8080/auth/logout', {
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
    this.http.get<any>(`http://localhost:8080/notes/${id}`).subscribe({
      next: (note) => {
        this.title = note.title;
        this.contentHtml = note.contentHtml;
        this.cdr.detectChanges(); // ← add this
      },
      error: (err) => console.error('Failed to fetch note', err),
    });
  }

  editNote(): void {
    this.router.navigate(['/edit', this.noteId]);
  }

  deleteNote(): void {
    if (!this.noteId) return;
    this.http.delete(`http://localhost:8080/notes/${this.noteId}`).subscribe({
      next: () => this.router.navigate(['/home']),
      error: (err) => console.error('Failed to delete note', err),
    });
  }

  goBack(): void {
    history.back();
  }
}
