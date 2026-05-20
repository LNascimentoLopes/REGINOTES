package LNascimento.Note_Taking_app.Services;

import LNascimento.Note_Taking_app.DTOs.NotesDTOs;
import LNascimento.Note_Taking_app.DTOs.NotesDTOs.*;
import LNascimento.Note_Taking_app.Models.Notes;
import LNascimento.Note_Taking_app.Repositories.NoteRepository;
import LNascimento.Note_Taking_app.Repositories.UserRepository;
import LNascimento.Note_Taking_app.Security.CustomUserDetails;
import LNascimento.Note_Taking_app.Utils.Mapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class NotesServices {
    private Mapper mapper;
    private NoteRepository repository;
    private Parser parser;
    private HtmlRenderer renderer;

    public NotesServices(UserRepository userRepository, Mapper mapper, NoteRepository repository, Parser parser, HtmlRenderer renderer)
    {
        this.mapper = mapper;
        this.repository = repository;
        this.parser = parser;
        this.renderer = renderer;
    }

    private String markToHtml(String markdown){
        return renderer.render(parser.parse(markdown));
    }

    @Transactional
    public Notes saveNote (NoteRequest request, CustomUserDetails user){

        Notes note = new Notes();

        note.setTitle(request.title());
        note.setContentMarkdown(request.content());
        note.setUsers(user.getUser());
        note.setCreatedAt(LocalDateTime.now());

        String Markdown = note.getContentMarkdown();
        String Html = markToHtml(Markdown);
        note.setContentHtml(Html);

        return repository.save(note);
    }

    public getNotesResponse getNoteById(UUID id, CustomUserDetails user){

        Notes note = repository.findByIdAndUserId(id,user.getId()).orElseThrow(() -> new EntityNotFoundException("Note not Found"));
        getNotesResponse response = mapper.NotesToDto(note);

        return response;
    }

    public Page<getNotesResponse> getAllNotes(CustomUserDetails user, Pageable pageable){

        Page<Notes> notesPage = repository.findByUserId(user.getId(),pageable).orElseThrow(() -> new EntityNotFoundException("Note not found"));
        Page<getNotesResponse> dtoPage = notesPage.map(notes -> mapper.NotesToDto(notes));

        return dtoPage;
    }

    @Transactional
    public void deleteNoteById(UUID id, CustomUserDetails user){
       repository.findByIdAndUserId(id,user.getId()).orElseThrow(() -> new EntityNotFoundException("Note not found"));
       repository.softDelete(id, user.getId());
    }
    @Transactional
    public Notes patchNote(UUID id, CustomUserDetails user, PatchNoteRequest request){

        Notes note = repository.findByIdAndUserId(id,user.getId()).orElseThrow();

        if (request.title().isPresent()){
            note.setTitle(request.title().get());
        }
        if (request.content().isPresent()){
            note.setContentMarkdown(request.content().get());
            note.setContentHtml(markToHtml(request.content().get()));
        }
        note.setUpdatedAt(LocalDateTime.now());

        return note;
    }

    public Page<getNotesResponse> getAllTrashedNotes(CustomUserDetails user, Pageable pageable){

        Page<Notes> notesPage = repository.findTrashedByUserId(user.getId(),pageable).orElseThrow(()->new EntityNotFoundException("Note not found"));
        Page<getNotesResponse> dtoPage = notesPage.map(notes -> mapper.NotesToDto(notes));

        return dtoPage;
    }

    @Transactional
    public void restoreNote(CustomUserDetails user, UUID id){
        Notes note = repository.findTrashedByIdAndUserId(id, user.getId()).orElseThrow(() -> new EntityNotFoundException("Note not found"));
        note.setDeletedAt(null);
    }
    @Transactional
    public void PermanentDelete(CustomUserDetails user, UUID id){
        repository.findTrashedByIdAndUserId(id,user.getId()).orElseThrow(() -> new EntityNotFoundException("Note not found"));
        repository.permaDelete(id,user.getId());
    }
}



