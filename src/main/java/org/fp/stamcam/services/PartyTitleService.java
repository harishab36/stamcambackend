package org.fp.stamcam.services;

import org.fp.stamcam.models.PartyTitle;
import org.fp.stamcam.repositories.PartyTitleRepository;
import org.fp.stamcam.utils.PartyTitleIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing PartyTitle entities.
 * Handles CRUD operations and business logic for party titles.
 */
@Service
public class PartyTitleService {

    @Autowired
    private PartyTitleRepository partyTitleRepository;

    @Autowired
    private PartyTitleIdGenerator partyTitleIdGenerator;

    /**
     * Create a new party title with auto-generated ID.
     *
     * @param title the title of the party
     * @return the created PartyTitle
     */
    public PartyTitle createPartyTitle(String title) {
        String id = partyTitleIdGenerator.generatePartyTitleId();
        PartyTitle partyTitle = PartyTitle.builder()
                .id(id)
                .title(title)
                .build();
        return partyTitleRepository.save(partyTitle);
    }

    /**
     * Get a party title by ID.
     *
     * @param id the party title ID
     * @return optional containing the party title if found
     */
    public Optional<PartyTitle> getPartyTitleById(String id) {
        return partyTitleRepository.findById(id);
    }

    /**
     * Get all party titles.
     *
     * @return list of all party titles
     */
    public List<PartyTitle> getAllPartyTitles() {
        return partyTitleRepository.findAll();
    }

    /**
     * Get party titles by title (case-insensitive search).
     *
     * @param title the title to search for
     * @return list of party titles matching the search
     */
    public List<PartyTitle> getPartyTitlesByTitle(String title) {
        return partyTitleRepository.findByTitleContainingIgnoreCase(title);
    }

    /**
     * Get a party title by exact title match.
     *
     * @param title the exact title
     * @return optional containing the party title if found
     */
    public Optional<PartyTitle> getPartyTitleByExactTitle(String title) {
        return partyTitleRepository.findByTitle(title);
    }

    /**
     * Update a party title.
     *
     * @param id the party title ID
     * @param title the new title
     * @return the updated PartyTitle, or empty if not found
     */
    public Optional<PartyTitle> updatePartyTitle(String id, String title) {
        Optional<PartyTitle> existingPartyTitle = partyTitleRepository.findById(id);
        if (existingPartyTitle.isPresent()) {
            PartyTitle partyTitle = existingPartyTitle.get();
            partyTitle.setTitle(title);
            return Optional.of(partyTitleRepository.save(partyTitle));
        }
        return Optional.empty();
    }

    /**
     * Delete a party title by ID.
     *
     * @param id the party title ID
     * @return true if the party title was deleted, false if not found
     */
    public boolean deletePartyTitle(String id) {
        if (partyTitleRepository.existsById(id)) {
            partyTitleRepository.deleteById(id);
            return true;
        }
        return false;
    }

}

