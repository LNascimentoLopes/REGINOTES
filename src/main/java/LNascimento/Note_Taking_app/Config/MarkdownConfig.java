package LNascimento.Note_Taking_app.Config;

import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.ParserContext;

@Configuration
public class MarkdownConfig {

    @Bean
    public Parser markdownParser(){

        return Parser.builder().build();
    }

    @Bean
    public HtmlRenderer htmlRenderer(){
        return HtmlRenderer.builder().build();
    }

}
