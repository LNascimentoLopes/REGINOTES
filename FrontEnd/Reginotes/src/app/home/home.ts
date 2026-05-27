import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { DatePipe, NgIf } from '@angular/common';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { environment } from '../../environments/environment';

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

interface Tag {
  id: string;
  tagName: string;
  tagColor: string;
}

interface TagPageResponse {
  content: Tag[];
  totalElements: number;
  totalPages: number;
}

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [DatePipe, RouterModule, FormsModule],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home implements OnInit {
  // All notes fetched from backend (unfiltered)
  allNotes: Note[] = [];
  // Displayed after filters applied
  notes: Note[] = [];
  recentNotes: Note[] = [];
  private readonly API_URL = environment.apiUrl;

  page = 0;
  pageSize = 32;
  totalPages = 0;

  // Search
  showSearch = false;
  searchQuery = '';

  // Tags
  showTagPopup = false;
  availableTags: Tag[] = [];
  selectedTags: Set<string> = new Set();

  // Sort
  sortDirection: 'asc' | 'desc' = 'desc';

  username = localStorage.getItem('username') ?? 'User';

  constructor(
    private http: HttpClient,
    private cdr: ChangeDetectorRef,
    public router: Router,
  ) {}

  ngOnInit(): void {
    this.fetchNotes();
    this.fetchRecents();
  }

  openNote(id: string): void {
    this.router.navigate(['/edit', id]);
  }

  // ── SEARCH ───────────────────────────────────
  toggleSearch(): void {
    this.showSearch = !this.showSearch;
    if (!this.showSearch) {
      this.searchQuery = '';
      this.page = 0;
      this.applyFilters();
    }
  }

  onSearchInput(): void {
    this.page = 0;
    this.applyFilters();
  }

  // ── TAGS ─────────────────────────────────────
  toggleTagPopup(): void {
    this.showTagPopup = !this.showTagPopup;
    if (this.showTagPopup && this.availableTags.length === 0) {
      this.fetchAllTags();
    }
  }

  fetchAllTags(): void {
    const params = new HttpParams().set('page', 0).set('size', 100);
    this.http.get<TagPageResponse>(`${this.API_URL}/notes/tags`, { params }).subscribe({
      next: (data) => {
        this.availableTags = data.content;
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Failed to fetch tags', err),
    });
  }

  toggleTag(tagId: string): void {
    if (this.selectedTags.has(tagId)) {
      this.selectedTags.delete(tagId);
    } else {
      this.selectedTags.add(tagId);
    }
    this.page = 0;
    this.applyFilters();
  }

  isTagSelected(tagId: string): boolean {
    return this.selectedTags.has(tagId);
  }

  clearTags(): void {
    this.selectedTags.clear();
    this.page = 0;
    this.applyFilters();
  }

  // ── SORT ─────────────────────────────────────
  toggleSort(): void {
    this.sortDirection = this.sortDirection === 'desc' ? 'asc' : 'desc';
    this.page = 0;
    this.fetchNotes(); // re-fetch sorted from backend
  }

  // ── FILTERS (client-side) ─────────────────────
  applyFilters(): void {
    let filtered = [...this.allNotes];

    if (this.searchQuery.trim()) {
      const q = this.searchQuery.trim().toLowerCase();
      filtered = filtered.filter(n => n.title.toLowerCase().includes(q));
    }

    if (this.selectedTags.size > 0) {
      filtered = filtered.filter(n =>
        n.tags?.some(t => this.selectedTags.has(t.id))
      );
    }

    this.totalPages = Math.ceil(filtered.length / this.pageSize);
    const start = this.page * this.pageSize;
    this.notes = filtered.slice(start, start + this.pageSize);
    this.cdr.detectChanges();
  }

  // ── FETCH ─────────────────────────────────────
  fetchNotes(): void {
    const params = new HttpParams()
      .set('page', 0)
      .set('size', 200)
      .set('sort', `updatedAt,${this.sortDirection}`);

    this.http.get<PageResponse>(`${this.API_URL}/notes`, { params }).subscribe({
      next: (data) => {
        this.allNotes = data.content;
        this.applyFilters();
      },
      error: (err) => console.error('Failed to fetch notes', err),
    });
  }

  fetchRecents(): void {
    const params = new HttpParams().set('page', 0).set('size', 4).set('sort', 'updatedAt,desc');
    this.http.get<PageResponse>(`${this.API_URL}/notes`, { params }).subscribe({
      next: (data) => {
        this.recentNotes = data.content;
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Failed to fetch recents', err),
    });
  }

  logout(): void {
    const refreshToken = localStorage.getItem('refreshToken');
    this.http.delete(`${this.API_URL}/auth/logout`, { body: { refreshToken } }).subscribe({
      next: () => this._clearAndRedirect(),
      error: () => this._clearAndRedirect(),
    });
  }

  private _clearAndRedirect(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('refreshToken');
    this.router.navigate(['/login']);
  }

  prevPage(): void {
    if (this.page > 0) { this.page--; this.applyFilters(); }
  }

  nextPage(): void {
    if (this.page + 1 < this.totalPages) { this.page++; this.applyFilters(); }
  }
}