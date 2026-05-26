import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Router } from '@angular/router';
import { DatePipe } from '@angular/common';

interface Note {
  id: string;
  title: string;
  updatedAt: string;
  deletedAt: string;
}

interface PageResponse {
  content: Note[];
  totalPages: number;
}

@Component({
  selector: 'app-trash',
  standalone: true,
  imports: [DatePipe],
  templateUrl: './trash.html',
  styleUrl: './trash.css'
})
export class Trash implements OnInit {
  notes: Note[] = [];
  currentPage = 0;
  totalPages = 0;
  username = localStorage.getItem('username') ?? 'User';

  constructor(public router: Router, private http: HttpClient, private cdr: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.fetchTrash();
  }

  fetchTrash(): void {
    const params = new HttpParams()
      .set('page', this.currentPage)
      .set('size', 6)
      .set('sort', 'deletedAt,desc');

    this.http.get<PageResponse>('http://localhost:8080/notes/trash', { params }).subscribe({
      next: (data) => {
        this.notes = data.content;
        this.totalPages = data.totalPages;
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Failed to fetch trash', err)
    });
  }

  restore(id: string): void {
    this.http.patch(`http://localhost:8080/notes/trash/${id}/restore`, {}).subscribe({
      next: () => this.fetchTrash(),
      error: (err) => console.error('Failed to restore note', err)
    });
  }

  deletePermanently(id: string): void {
    this.http.delete(`http://localhost:8080/notes/trash/${id}/delete`).subscribe({
      next: () => this.fetchTrash(),
      error: (err) => console.error('Failed to delete note', err)
    });
  }

  prevPage(): void {
    if (this.currentPage > 0) {
      this.currentPage--;
      this.fetchTrash();
    }
  }

  nextPage(): void {
    if (this.currentPage < this.totalPages - 1) {
      this.currentPage++;
      this.fetchTrash();
    }
  }

  logout(): void {
    const refreshToken = localStorage.getItem('refreshToken');
    this.http.delete('http://localhost:8080/auth/logout', { body: { refreshToken } }).subscribe({
      next: () => this._clearAndRedirect(),
      error: () => this._clearAndRedirect()
    });
  }

  private _clearAndRedirect(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('refreshToken');
    this.router.navigate(['/login']);
  }
}