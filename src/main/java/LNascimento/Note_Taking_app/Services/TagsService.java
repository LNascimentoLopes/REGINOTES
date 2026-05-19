package LNascimento.Note_Taking_app.Services;

import LNascimento.Note_Taking_app.DTOs.TagDTOs.*;
import LNascimento.Note_Taking_app.DTOs.NotesDTOs.*;
import LNascimento.Note_Taking_app.Models.Notes;
import LNascimento.Note_Taking_app.Models.Tags;
import LNascimento.Note_Taking_app.Repositories.NoteRepository;
import LNascimento.Note_Taking_app.Repositories.TagRepository;
import LNascimento.Note_Taking_app.Repositories.UserRepository;
import LNascimento.Note_Taking_app.Security.CustomUserDetails;
import LNascimento.Note_Taking_app.Utils.Mapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TagsService {

    @Autowired
    private NoteRepository nRepository;
    @Autowired
    private TagRepository tRepository;
    @Autowired
    private Mapper mapper;


    @Transactional
    public void assignTag(CustomUserDetails user, UUID uuid, CreateTagRequest request){

        Notes note = nRepository.findByIdAndUserId(uuid, user.getId()).orElseThrow(() -> new EntityNotFoundException("Note not Found"));
        Tags tags = tRepository.findByTagNameAndUserId(request.tagName(),user.getId())
                .orElseGet(() ->{
                    Tags newTag = mapper.TagsDtoToEntity(request,user);
                    return tRepository.save(newTag);});
        note.getTags().add(tags);
    }

    @Transactional
    public void removeTagAssignment (CustomUserDetails user, UUID tagId, UUID noteId){

        Notes note = nRepository.findByIdAndUserId(noteId,user.getId()).orElseThrow(() -> new EntityNotFoundException("Note not Found"));
        Tags tag = tRepository.findByIdAndUserId(tagId, user.getId()).orElseThrow(() -> new EntityNotFoundException("Tag not Found"));
        note.getTags().remove(tag);
    }

    @Transactional
    public void deleteTag (CustomUserDetails user, UUID tagId){
        Tags tag = tRepository.findByIdAndUserId(tagId,user.getId()).orElseThrow(() -> new EntityNotFoundException("note not found"));

        if (tRepository.isTagAssignedToAnyNote(tagId)){
            throw new DataIntegrityViolationException("Tag assigned to note");
        }

        tRepository.delete(tag);
    }

    public Page<TagResponseDTO> getTags (CustomUserDetails user, Pageable pageable){

        Page<Tags> tagsPage = tRepository.findAllByUserId(user.getId(),pageable).orElseThrow(() -> new EntityNotFoundException("No tags Found"));

        Page<TagResponseDTO> mapped = tagsPage.map(tags -> mapper.TagToDTO(tags));

        return mapped;

    }
}
