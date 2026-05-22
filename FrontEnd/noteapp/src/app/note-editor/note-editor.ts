import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NoteService } from './Note.service';

@Component({
  selector: 'app-note-editor',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './note-editor.html',
  styleUrls: ['./note-editor.css'],
})
export class NoteEditorComponent {
  markdownContent = '';
  renderedHtml = '';
  errorMessage = '';
  isLoading = false;
  isSaved = false;
  noteId: string | null = null;
  isPreviewMode = false;

  constructor(private noteService: NoteService) {}

  save(): void {
    if (!this.markdownContent.trim()) return;
    this.isLoading = true;
    this.errorMessage = '';

    const request$ = this.noteId
      ? this.noteService.updateNote(this.noteId, this.markdownContent) // PUT
      : this.noteService.convertMarkdown(this.markdownContent); // POST

    request$.subscribe({
      next: (response) => {
        this.renderedHtml = response.ContentHtml;
        this.noteId = response.id; // 👈 store the id after first save
        this.isLoading = false;
        this.isPreviewMode = true;
      },
      error: (err) => {
        this.errorMessage = 'Failed to save note. Please try again.';
        this.isLoading = false;
        console.error(err);
      },
    });
  }

  edit(): void {
    this.isPreviewMode = false; // 👈 go back to editor
  }

  clear(): void {
    this.markdownContent = '';
    this.renderedHtml = '';
    this.errorMessage = '';
    this.isPreviewMode = false;
    this.noteId = null;
  }
}
