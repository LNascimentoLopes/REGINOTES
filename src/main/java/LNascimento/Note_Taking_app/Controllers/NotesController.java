package LNascimento.Note_Taking_app.Controllers;

import LNascimento.Note_Taking_app.DTOs.NotesDTOs.*;
import LNascimento.Note_Taking_app.DTOs.TagDTOs;
import LNascimento.Note_Taking_app.Models.Notes;
import LNascimento.Note_Taking_app.Security.CustomUserDetails;
import LNascimento.Note_Taking_app.Services.NotesServices;
import LNascimento.Note_Taking_app.Services.TagsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/notes")
public class NotesController {

    @Autowired
    private NotesServices nServices;
    @Autowired
    private TagsService tServices;

    @PostMapping
    public ResponseEntity<Map<String,String>> Create(
            @RequestBody NoteRequest request,
            @AuthenticationPrincipal CustomUserDetails user){

            Notes note = nServices.saveNote(request,user);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("html", note.getContentHtml()));
    }
    @GetMapping
    public ResponseEntity<Page<getNotesResponse>> GetAllNotes(
            @RequestParam(required = false)String tag,
            @AuthenticationPrincipal CustomUserDetails user,
            Pageable pageable){
            Page<getNotesResponse> allNotes = nServices.getAllNotes(user, pageable);
            return ResponseEntity.status(HttpStatus.OK).body(allNotes);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity <getNotesResponse> GetNoteById(@PathVariable UUID uuid, @AuthenticationPrincipal CustomUserDetails user ){
            getNotesResponse notes = nServices.getNoteById(uuid, user);
            return ResponseEntity.ok(notes);
    }
    @DeleteMapping("/{uuid}")
    public ResponseEntity DeleteNote(@PathVariable UUID uuid, @AuthenticationPrincipal CustomUserDetails user){

            nServices.deleteNoteById(uuid, user);
            return ResponseEntity.status(HttpStatus.OK).build();

    }

    @PatchMapping("/{uuid}")
    public ResponseEntity PatchNote(@PathVariable UUID uuid,@RequestBody PatchNoteRequest request, @AuthenticationPrincipal CustomUserDetails user){
            nServices.patchNote(uuid,user,request);
            return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping ("/trash")
    public ResponseEntity<Page<getNotesResponse>> GetAllTrashedNotes(@RequestParam(required = false)String tag,
                                             @AuthenticationPrincipal CustomUserDetails user,
                                             Pageable pageable){
            Page<getNotesResponse> allNotes = nServices.getAllTrashedNotes(user, pageable);
            return ResponseEntity.status(HttpStatus.OK).body(allNotes);
    }
    @PatchMapping ("/trash/{uuid}/restore")
    public ResponseEntity RestoreNotes(@PathVariable UUID uuid,@AuthenticationPrincipal CustomUserDetails user){
        nServices.restoreNote(user,uuid);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @DeleteMapping("/trash/{uuid}/delete")
    public ResponseEntity DeletePermanent(@PathVariable UUID uuid, @AuthenticationPrincipal CustomUserDetails user){
        nServices.PermanentDelete(user,uuid);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @PostMapping("/{uuid}/tags")
    public ResponseEntity AssignTag(@PathVariable UUID uuid, @RequestBody TagDTOs.CreateTagRequest request, @AuthenticationPrincipal CustomUserDetails user){
        tServices.assignTag(user,uuid,request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


}
