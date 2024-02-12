package com.alibou.security.bookbinder;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v2/books")
@RequiredArgsConstructor
public class BookbinderController {

    private final BookbinderService service;

    @PostMapping
    public ResponseEntity<?> save(
            @RequestBody BookbinderRequest request
    ) {
        service.save(request);
        return ResponseEntity.accepted().build();
    }

    @GetMapping
    public ResponseEntity<List<Bookbinder>> findAllBooks() {
        return ResponseEntity.ok(service.findAll());
    }
}
