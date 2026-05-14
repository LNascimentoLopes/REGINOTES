package LNascimento.Note_Taking_app.Utils;

import LNascimento.Note_Taking_app.DTOs.AuthDTOs.registerRequest;
import LNascimento.Note_Taking_app.DTOs.TagDTOs.*;
import LNascimento.Note_Taking_app.DTOs.NotesDTOs.*;
import LNascimento.Note_Taking_app.Models.Notes;
import LNascimento.Note_Taking_app.Models.Tags;
import LNascimento.Note_Taking_app.Models.Users;
import LNascimento.Note_Taking_app.Security.CustomUserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class Mapper {


    public Users RegisterDtoToEntity(registerRequest request, PasswordEncoder encoder){
        Users user = new Users();

        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(encoder.encode(request.password()));
        user.setRole(Roles.USER);

        return user;
    }

    public getNotesResponse NotesToDto(Notes note ){
       getNotesResponse response = new getNotesResponse(
               note.getTitle(),
               note.getContentMarkdown(),
               note.getContentHtml(),
               note.getCreatedAt(),
               note.getUpdatedAt(),
               note.getDeletedAt(),
               note.getId(),
               note.getTags()
                       .stream()
                       .map(Tags::getTagName)
                       .collect(Collectors.toSet()));



        return response;

    }

    public Tags TagsDtoToEntity(CreateTagRequest request, CustomUserDetails user) {
        Tags tag = new Tags();

        tag.setTagName(request.tagName());
        tag.setTagColor(request.tagColor());
        tag.setUser(user.getUser());

        return tag;
    }
}
