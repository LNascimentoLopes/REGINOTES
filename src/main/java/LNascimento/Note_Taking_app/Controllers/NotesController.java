package LNascimento.Note_Taking_app.Controllers;

import LNascimento.Note_Taking_app.DTOs.NotesDTOs.*;
import LNascimento.Note_Taking_app.DTOs.TagDTOs.*;
import LNascimento.Note_Taking_app.Models.Notes;
import LNascimento.Note_Taking_app.Models.Tags;
import LNascimento.Note_Taking_app.Security.CustomUserDetails;
import LNascimento.Note_Taking_app.Services.NotesServices;
import LNascimento.Note_Taking_app.Services.TagsService;
import jakarta.validation.Valid;
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


    //basic crud
    @PostMapping
    public ResponseEntity<Map<String,String>> Create(@RequestBody NoteRequest request, @AuthenticationPrincipal CustomUserDetails user){

            Notes note = nServices.saveNote(request,user);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("html", note.getContentHtml()));
    }
    @GetMapping
    public ResponseEntity<Page<getNotesResponse>> GetAllNotes(@RequestParam(required = false)String tag, @AuthenticationPrincipal CustomUserDetails user, Pageable pageable){
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

    //trash crud
    @GetMapping ("/trash")
    public ResponseEntity<Page<getNotesResponse>> GetAllTrashedNotes(@RequestParam(required = false)String tag, @AuthenticationPrincipal CustomUserDetails user, Pageable pageable){
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

    //tags crud
    @PostMapping("/{uuid}/tags")
    public ResponseEntity AssignTag(@PathVariable UUID uuid, @RequestBody @Valid CreateTagRequest request, @AuthenticationPrincipal CustomUserDetails user){
        tServices.assignTag(user,uuid,request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @DeleteMapping("/{uuid}/tags/{tagId}")
    public ResponseEntity RemoveAssignedTag(@PathVariable UUID uuid , @PathVariable UUID tagId, @AuthenticationPrincipal CustomUserDetails user){
        tServices.removeTagAssignment(user,tagId,uuid);
        return ResponseEntity.ok("");
    }
    @DeleteMapping("tags/{uuid}")
    public ResponseEntity DeleteTag(@PathVariable UUID uuid, @AuthenticationPrincipal CustomUserDetails user){
        tServices.deleteTag(user,uuid);
        return ResponseEntity.ok("");
    }
    @GetMapping("/tags")
    public ResponseEntity<Page<TagResponseDTO>> GetAllTags(@AuthenticationPrincipal CustomUserDetails user, Pageable pageable){
        Page<TagResponseDTO> tags = tServices.getTags(user, pageable);

        return ResponseEntity.ok(tags);
    }


}
