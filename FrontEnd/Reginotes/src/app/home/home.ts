import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { NgFor, NgIf, DatePipe } from '@angular/common';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Router, RouterModule } from '@angular/router';

interface Note {
  id: string;
  title: string;
  contentMarkdown: string;
  contentHtml: string;
  createdAt: string;
  updatedAt: string;
  deletedAt: string | null;
  tags: { id: string; name: string }[];
}

interface PageResponse {
  content: Note[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [DatePipe, RouterModule],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home implements OnInit {
  notes: Note[] = [];
  recentNotes: Note[] = [];
  page = 0;
  pageSize = 50;
  totalPages = 0;

  constructor(
    private http: HttpClient,
    private cdr: ChangeDetectorRef,
    public router: Router,
  ) {}

  openNote(id: string): void {
    this.router.navigate(['/edit', id]);
  }

  ngOnInit(): void {
    this.fetchNotes();
    this.fetchRecents();
  }

  fetchNotes(): void {
    const params = new HttpParams().set('page', this.page).set('size', this.pageSize);

    this.http.get<PageResponse>('http://localhost:8080/notes', { params }).subscribe({
      next: (data) => {
        this.notes = data.content;
        this.totalPages = data.totalPages;
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Failed to fetch notes', err),
    });
  }

  username = localStorage.getItem('username') ?? 'User';

  logout(): void {
    const refreshToken = localStorage.getItem('refreshToken');

    this.http
      .delete('http://localhost:8080/auth/logout', {
        body: { refreshToken },
      })
      .subscribe({
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
        },
      });
  }

  fetchRecents(): void {
    const params = new HttpParams().set('page', 0).set('size', 4).set('sort', 'createdAt,desc'); // 👈 add this

    this.http.get<PageResponse>('http://localhost:8080/notes', { params }).subscribe({
      next: (data) => {
        this.recentNotes = data.content;
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Failed to fetch recents', err),
    });
  }

  prevPage(): void {
    if (this.page > 0) {
      this.page--;
      this.fetchNotes();
    }
  }

  nextPage(): void {
    if (this.page + 1 < this.totalPages) {
      this.page++;
      this.fetchNotes();
    }
  }
}
