package com.alibou.security.bookbinder;

import com.alibou.security.auth.AuthenticationService;
import com.alibou.security.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository repository;
    private final AuthenticationService authenticationService;

    public void save(BookRequest request) {
        var book = Book.builder()
                .id(request.getId())
                .author(request.getAuthor())
                .isbn(request.getIsbn())
                .build();
        repository.save(book);
    }

    public List<Book> findAll() {
        Integer currentUserId = getCurrentUserId();
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("createdBy", matcher -> matcher.ignoreCase().exact())
                .withIgnorePaths("id", "author", "isbn", "createDate", "lastModified", "lastModifiedBy");
        Book exampleBook = Book.builder().createdBy(currentUserId).build();
        Example<Book> example = Example.of(exampleBook, exampleMatcher);
        return repository.findAll(example);
    }

    private Integer getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            User user = authenticationService.getUserByEmail(email);
            return user.getId();
        }
        return null;
    }
}
