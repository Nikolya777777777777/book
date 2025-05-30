package com.example.demo.repository.book;

import com.example.demo.exception.SpecificationProviderManagerException;
import com.example.demo.model.Book;
import com.example.demo.repository.SpecificationProvider;
import com.example.demo.repository.SpecificationProviderManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationProviderManager implements SpecificationProviderManager<Book> {
    private final List<SpecificationProvider<Book>> bookSpecificationProviders;

    @Override
    public SpecificationProvider<Book> getSpecificationProvider(String key) {
        return bookSpecificationProviders.stream()
                .filter(p -> p.getKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new SpecificationProviderManagerException("Can't find "
                        + "correct specification provider for key " + key));
    }
}
