import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, switchMap } from 'rxjs';

export interface MarkdownResponse {
  ContentHtml: string;
  ContentMarkdown: string;
  Title: string;
  id: string;
}

@Injectable({
  providedIn: 'root',
})
export class NoteService {
  // 🔧 Change this to your backend URL
  private readonly apiUrl = 'http://localhost:8080/notes';
  constructor(private http: HttpClient) {}
  convertMarkdown(markdown: string): Observable<MarkdownResponse> {

    return this.http.post<MarkdownResponse>(this.apiUrl, {title: "tituloTeste",Content: markdown});
  }

  updateNote(id: string, markdown: string): Observable<MarkdownResponse> {
  return this.http.put<MarkdownResponse>(`${this.apiUrl}/${id}`, {
    title: "tituloTeste",
    Content: markdown
  });
}


}