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
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TagsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NoteRepository nRepository;
    @Autowired
    private TagRepository tRepository;
    @Autowired
    private Parser parser;
    @Autowired
    private HtmlRenderer renderer;
    @Autowired
    private Mapper mapper;


    @Transactional
    public void assignTag(CustomUserDetails user, UUID uuid, CreateTagRequest request){

        Notes note = nRepository.findByIdAndUserId(uuid,user.getId()).orElseThrow(() -> new EntityNotFoundException("Note not Found"));

        Tags tag = tRepository.findByTagNameAndUserId(request.tagName(),user.getId())
                .orElseGet(() ->{
                    Tags newTag = mapper.TagsDtoToEntity(request,user);
                    return tRepository.save(newTag);});

    }
}
