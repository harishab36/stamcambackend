package org.fp.stamcam.services;

import org.fp.stamcam.models.DeedTitle;
import org.fp.stamcam.models.PartyTitle;
import org.fp.stamcam.repositories.DeedTitleRepository;
import org.fp.stamcam.repositories.PartyTitleRepository;
import org.fp.stamcam.utils.DeedTitleIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing DeedTitle entities.
 * Handles CRUD operations and business logic for deed titles, including management of associated party titles.
 */
@Service
public class DeedTitleService {

    @Autowired
    private DeedTitleRepository deedTitleRepository;

    @Autowired
    private PartyTitleRepository partyTitleRepository;

    @Autowired
    private DeedTitleIdGenerator deedTitleIdGenerator;

    /**
     * Create a new deed title with auto-generated ID.
     *
     * @param title the title of the deed
     * @return the created DeedTitle
     */
    public DeedTitle createDeedTitle(String title) {
        String id = deedTitleIdGenerator.generateDeedTitleId();
        DeedTitle deedTitle = DeedTitle.builder()
                .id(id)
                .title(title)
                .build();
        return deedTitleRepository.save(deedTitle);
    }

    /**
     * Get a deed title by ID.
     *
     * @param id the deed title ID
     * @return optional containing the deed title if found
     */
    public Optional<DeedTitle> getDeedTitleById(String id) {
        return deedTitleRepository.findById(id);
    }

    /**
     * Get all deed titles.
     *
     * @return list of all deed titles
     */
    public List<DeedTitle> getAllDeedTitles() {
        return deedTitleRepository.findAll();
    }

    /**
     * Get deed titles by title (case-insensitive search).
     *
     * @param title the title to search for
     * @return list of deed titles matching the search
     */
    public List<DeedTitle> getDeedTitlesByTitle(String title) {
        return deedTitleRepository.findByTitleContainingIgnoreCase(title);
    }

    /**
     * Get a deed title by exact title match.
     *
     * @param title the exact title
     * @return optional containing the deed title if found
     */
    public Optional<DeedTitle> getDeedTitleByExactTitle(String title) {
        return deedTitleRepository.findByTitle(title);
    }

    /**
     * Update a deed title.
     *
     * @param id the deed title ID
     * @param title the new title
     * @return the updated DeedTitle, or empty if not found
     */
    public Optional<DeedTitle> updateDeedTitle(String id, String title) {
        Optional<DeedTitle> existingDeedTitle = deedTitleRepository.findById(id);
        if (existingDeedTitle.isPresent()) {
            DeedTitle deedTitle = existingDeedTitle.get();
            deedTitle.setTitle(title);
            return Optional.of(deedTitleRepository.save(deedTitle));
        }
        return Optional.empty();
    }

    /**
     * Delete a deed title by ID.
     *
     * @param id the deed title ID
     * @return true if the deed title was deleted, false if not found
     */
    public boolean deleteDeedTitle(String id) {
        if (deedTitleRepository.existsById(id)) {
            deedTitleRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Add a party title to a deed title.
     *
     * @param deedTitleId the deed title ID
     * @param partyTitleId the party title ID to add
     * @return the updated DeedTitle, or empty if deed title not found
     */
    public Optional<DeedTitle> addPartyTitle(String deedTitleId, String partyTitleId) {
        Optional<DeedTitle> deedTitleOpt = deedTitleRepository.findById(deedTitleId);
        Optional<PartyTitle> partyTitleOpt = partyTitleRepository.findById(partyTitleId);

        if (deedTitleOpt.isPresent() && partyTitleOpt.isPresent()) {
            DeedTitle deedTitle = deedTitleOpt.get();
            PartyTitle partyTitle = partyTitleOpt.get();

            // Check if party title is already added
            if (deedTitle.getPartyTitles().stream()
                    .noneMatch(pt -> pt.getId().equals(partyTitleId))) {
                deedTitle.getPartyTitles().add(partyTitle);
                return Optional.of(deedTitleRepository.save(deedTitle));
            }
            // Return the deed title without modification if party title already exists
            return Optional.of(deedTitle);
        }
        return Optional.empty();
    }

    /**
     * Remove a party title from a deed title.
     *
     * @param deedTitleId the deed title ID
     * @param partyTitleId the party title ID to remove
     * @return the updated DeedTitle, or empty if deed title not found
     */
    public Optional<DeedTitle> removePartyTitle(String deedTitleId, String partyTitleId) {
        Optional<DeedTitle> deedTitleOpt = deedTitleRepository.findById(deedTitleId);

        if (deedTitleOpt.isPresent()) {
            DeedTitle deedTitle = deedTitleOpt.get();
            deedTitle.getPartyTitles().removeIf(pt -> pt.getId().equals(partyTitleId));
            return Optional.of(deedTitleRepository.save(deedTitle));
        }
        return Optional.empty();
    }

    /**
     * Update a party title within a deed title.
     *
     * @param deedTitleId the deed title ID
     * @param partyTitleId the party title ID to update
     * @param newTitle the new title for the party
     * @return the updated DeedTitle, or empty if not found
     */
    public Optional<DeedTitle> updatePartyTitleInDeed(String deedTitleId, String partyTitleId, String newTitle) {
        Optional<DeedTitle> deedTitleOpt = deedTitleRepository.findById(deedTitleId);

        if (deedTitleOpt.isPresent()) {
            DeedTitle deedTitle = deedTitleOpt.get();
            boolean updated = deedTitle.getPartyTitles().stream()
                    .filter(pt -> pt.getId().equals(partyTitleId))
                    .findFirst()
                    .map(pt -> {
                        pt.setTitle(newTitle);
                        return true;
                    })
                    .orElse(false);

            if (updated) {
                return Optional.of(deedTitleRepository.save(deedTitle));
            }
        }
        return Optional.empty();
    }

    /**
     * Get all party titles for a deed title.
     *
     * @param deedTitleId the deed title ID
     * @return list of party titles, or empty list if deed title not found
     */
    public List<PartyTitle> getPartyTitlesForDeed(String deedTitleId) {
        Optional<DeedTitle> deedTitleOpt = deedTitleRepository.findById(deedTitleId);
        return deedTitleOpt.map(DeedTitle::getPartyTitles).orElse(List.of());
    }

}

