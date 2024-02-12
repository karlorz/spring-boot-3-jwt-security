package com.alibou.security.bookbinder;

import com.alibou.security.auth.AuthenticationService;
import com.alibou.security.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookbinderService {

    private final BookbinderRepository repository;
    private final AuthenticationService authenticationService;

    public void save(BookbinderRequest request) {
        Integer currentUserId = getCurrentUserId();
        if (currentUserId != null) {
            if (request.getId() == null) {
                // Save a new book
                var book = Bookbinder.builder()
                        .author(request.getAuthor())
                        .isbn(request.getIsbn())
                        .createdBy(currentUserId)
                        .build();
                repository.save(book);
            } else {
                // Update an existing book
                Bookbinder existingBook = repository.findById(request.getId()).orElse(null);
                if (existingBook == null) {
                    log.info("The book with the specified ID does not exist");
                }
                if (existingBook.getCreatedBy().equals(currentUserId)) {
                    existingBook.setAuthor(request.getAuthor());
                    existingBook.setIsbn(request.getIsbn());
                    repository.save(existingBook);
                } else {
                    log.info("The record is not owned by the current user");
                }
            }
        } else {
            log.info("Current user ID not available");
        }
    }

    public List<Bookbinder> findAll() {
        Integer currentUserId = getCurrentUserId();
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("createdBy", matcher -> matcher.ignoreCase().exact())
                .withIgnorePaths("id", "author", "isbn", "createDate", "lastModified", "lastModifiedBy");
        Bookbinder exampleBook = Bookbinder.builder().createdBy(currentUserId).build();
        Example<Bookbinder> example = Example.of(exampleBook, exampleMatcher);
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
