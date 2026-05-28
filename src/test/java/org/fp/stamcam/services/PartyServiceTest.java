package org.fp.stamcam.services;

import org.fp.stamcam.models.Document;
import org.fp.stamcam.models.Party;
import org.fp.stamcam.models.PartyType;
import org.fp.stamcam.models.IdType;
import org.fp.stamcam.repositories.PartyRepository;
import org.fp.stamcam.utils.PartyIdGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for PartyService class.
 */
@ExtendWith(MockitoExtension.class)
public class PartyServiceTest {

    @Mock
    private PartyRepository partyRepository;

    @Mock
    private PartyIdGenerator partyIdGenerator;

    @InjectMocks
    private PartyService partyService;

    /**
     * Test creating a new party.
     */
    @Test
    public void testCreateParty() {
        Party party = Party.builder()
                .name("John Doe")
                .emailId("john@example.com")
                .phoneNumber("9876543210")
                .documents(Arrays.asList(new Document( "DOX00000001", IdType.ADDRESS_PROOF, "base64ImageData")))
                .partyType(PartyType.FIRST_PARTY)
                .build();

        when(partyIdGenerator.generatePartyId()).thenReturn("PT00000001");
        when(partyRepository.save(any(Party.class))).thenReturn(party);

        Party createdParty = partyService.createParty(party);

        assertNotNull(createdParty);
        assertEquals("John Doe", createdParty.getName());
        assertEquals(IdType.IDENTITY_PROOF, createdParty.getDocuments().get(0).getIdType());
        verify(partyRepository, times(1)).save(any(Party.class));
    }

    /**
     * Test retrieving a party by ID.
     */
    @Test
    public void testGetPartyById() {
        Party party = Party.builder()
                .id("PT00000001")
                .name("Jane Doe")
                .emailId("jane@example.com")
                .phoneNumber("9876543211")
                .documents(Arrays.asList(new Document( "DOX00000001", IdType.ADDRESS_PROOF, "base64ImageData")))
                .partyType(PartyType.SECOND_PARTY)
                .build();

        when(partyRepository.findById("PT00000001")).thenReturn(Optional.of(party));

        Optional<Party> foundParty = partyService.getPartyById("PT00000001");

        assertTrue(foundParty.isPresent());
        assertEquals("Jane Doe", foundParty.get().getName());
    }

    /**
     * Test retrieving a party by ID when not found.
     */
    @Test
    public void testGetPartyByIdNotFound() {
        when(partyRepository.findById("PT00000999")).thenReturn(Optional.empty());

        Optional<Party> foundParty = partyService.getPartyById("PT00000999");

        assertFalse(foundParty.isPresent());
    }

    /**
     * Test retrieving all parties.
     */
    @Test
    public void testGetAllParties() {
        List<Party> parties = Arrays.asList(
                Party.builder().id("PT00000001").name("Party 1").build(),
                Party.builder().id("PT00000002").name("Party 2").build()
        );

        when(partyRepository.findAll()).thenReturn(parties);

        List<Party> allParties = partyService.getAllParties();

        assertEquals(2, allParties.size());
        verify(partyRepository, times(1)).findAll();
    }

    /**
     * Test updating a party.
     */
    @Test
    public void testUpdateParty() {
        Party existingParty = Party.builder()
                .id("PT00000001")
                .name("John Doe")
                .emailId("john@example.com")
                .phoneNumber("9876543210")
                .documents(Arrays.asList(new Document( "DOX00000001", IdType.IDENTITY_PROOF, "base64ImageData")))
                .partyType(PartyType.FIRST_PARTY)
                .build();

        Party updatedDetails = Party.builder()
                .name("John Updated")
                .emailId("john.updated@example.com")
                .build();

        when(partyRepository.findById("PT00000001")).thenReturn(Optional.of(existingParty));
        when(partyRepository.save(any(Party.class))).thenReturn(existingParty);

        Optional<Party> updatedParty = partyService.updateParty("PT00000001", updatedDetails);

        assertTrue(updatedParty.isPresent());
        verify(partyRepository, times(1)).save(any(Party.class));
    }

    /**
     * Test deleting a party.
     */
    @Test
    public void testDeleteParty() {
        when(partyRepository.existsById("PT00000001")).thenReturn(true);

        boolean deleted = partyService.deleteParty("PT00000001");

        assertTrue(deleted);
        verify(partyRepository, times(1)).deleteById("PT00000001");
    }

    /**
     * Test deleting a party when not found.
     */
    @Test
    public void testDeletePartyNotFound() {
        when(partyRepository.existsById("PT00000999")).thenReturn(false);

        boolean deleted = partyService.deleteParty("PT00000999");

        assertFalse(deleted);
    }

    /**
     * Test getting parties by party type.
     */
    @Test
    public void testGetPartiesByType() {
        List<Party> parties = Arrays.asList(
                Party.builder().id("PT00000001").partyType(PartyType.FIRST_PARTY).build(),
                Party.builder().id("PT00000002").partyType(PartyType.FIRST_PARTY).build()
        );

        when(partyRepository.findByPartyType(PartyType.FIRST_PARTY)).thenReturn(parties);

        List<Party> foundParties = partyService.getPartiesByType(PartyType.FIRST_PARTY);

        assertEquals(2, foundParties.size());
        verify(partyRepository, times(1)).findByPartyType(PartyType.FIRST_PARTY);
    }

}