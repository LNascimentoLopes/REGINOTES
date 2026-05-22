import { Component } from '@angular/core';
import { NoteEditorComponent } from './note-editor/note-editor';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [NoteEditorComponent],
  template: `<app-note-editor />`,
})
export class AppComponent {}