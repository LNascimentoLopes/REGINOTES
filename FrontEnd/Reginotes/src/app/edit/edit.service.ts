import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, switchMap } from 'rxjs';

export interface MarkdownResponse {
  id: string;
  title: string;
  contentMarkdown: string;
  contentHtml: string;
  createdAt: string;
  updatedAt: string;
  deletedAt: string | null;
  tags: { id: string; name: string }[];
}

@Injectable({
  providedIn: 'root',
})
export class NoteService {
  // 🔧 Change this to your backend URL
  private readonly apiUrl = 'http://localhost:8080/notes';
  constructor(private http: HttpClient) {}

  getNoteById(uuid: string): Observable<MarkdownResponse> {
    return this.http.get<MarkdownResponse>(`${this.apiUrl}/${uuid}`);
  }

  convertMarkdown(title: string, markdown: string): Observable<MarkdownResponse> {
    return this.http.post<MarkdownResponse>(this.apiUrl, { title: title, content: markdown });
  }

  updateNote(uuid: string, title: string, markdown: string): Observable<MarkdownResponse> {
    return this.http.patch<MarkdownResponse>(`${this.apiUrl}/${uuid}`, {
      title: title,
      content: markdown,
    });
  }
}
