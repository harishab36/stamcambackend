package org.fp.stamcam.services;

import org.fp.stamcam.models.Deed;
import org.fp.stamcam.models.DeedType;
import org.fp.stamcam.models.Party;
import org.fp.stamcam.repositories.DeedRepository;
import org.fp.stamcam.utils.DeedIdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for DeedService.
 */
@ExtendWith(MockitoExtension.class)
public class DeedServiceTest {

    @Mock
    private DeedRepository deedRepository;

    @Mock
    private DeedIdGenerator deedIdGenerator;

    @InjectMocks
    private DeedService deedService;

    private Deed testDeed;

    @BeforeEach
    void setUp() {
        testDeed = Deed.builder()
                .id("DD00000001")
                .title("Property Transfer Agreement")
                .matter("This is a legal document for property transfer...")
                .type(DeedType.SALE_DEED)
                .parties(Arrays.asList(Party.builder().id("PT00000001").name("John Doe").build()))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void testGetAllDeeds() {
        List<Deed> deeds = Arrays.asList(testDeed);
        when(deedRepository.findAll()).thenReturn(deeds);

        List<Deed> result = deedService.getAllDeeds();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("DD00000001", result.get(0).getId());
        verify(deedRepository, times(1)).findAll();
    }

    @Test
    void testGetDeedById() {
        when(deedRepository.findById("DD00000001")).thenReturn(Optional.of(testDeed));

        Optional<Deed> result = deedService.getDeedById("DD00000001");

        assertTrue(result.isPresent());
        assertEquals("Property Transfer Agreement", result.get().getTitle());
        verify(deedRepository, times(1)).findById("DD00000001");
    }

    @Test
    void testCreateDeed() {
        when(deedIdGenerator.generateDeedId()).thenReturn("DD00000001");
        when(deedRepository.save(any(Deed.class))).thenReturn(testDeed);

        Deed result = deedService.createDeed(testDeed);

        assertNotNull(result);
        assertEquals("DD00000001", result.getId());
        assertEquals("Property Transfer Agreement", result.getTitle());
        verify(deedIdGenerator, times(1)).generateDeedId();
        verify(deedRepository, times(1)).save(any(Deed.class));
    }

    @Test
    void testUpdateDeed() {
        Deed updatedDeed = Deed.builder()
                .title("Updated Property Transfer")
                .type(DeedType.GIFT_DEED)
                .build();

        when(deedRepository.findById("DD00000001")).thenReturn(Optional.of(testDeed));
        when(deedRepository.save(any(Deed.class))).thenReturn(testDeed);

        Optional<Deed> result = deedService.updateDeed("DD00000001", updatedDeed);

        assertTrue(result.isPresent());
        verify(deedRepository, times(1)).findById("DD00000001");
        verify(deedRepository, times(1)).save(any(Deed.class));
    }

    @Test
    void testDeleteDeed() {
        doNothing().when(deedRepository).deleteById("DD00000001");

        deedService.deleteDeed("DD00000001");

        verify(deedRepository, times(1)).deleteById("DD00000001");
    }

    @Test
    void testGetDeedsByType() {
        List<Deed> deeds = Arrays.asList(testDeed);
        when(deedRepository.findByType(DeedType.SALE_DEED)).thenReturn(deeds);

        List<Deed> result = deedService.getDeedsByType(DeedType.SALE_DEED);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(DeedType.SALE_DEED, result.get(0).getType());
        verify(deedRepository, times(1)).findByType(DeedType.SALE_DEED);
    }

    @Test
    void testSearchDeedsByTitle() {
        List<Deed> deeds = Arrays.asList(testDeed);
        when(deedRepository.findByTitleContainingIgnoreCase("Property")).thenReturn(deeds);

        List<Deed> result = deedService.searchDeedsByTitle("Property");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getTitle().contains("Property"));
        verify(deedRepository, times(1)).findByTitleContainingIgnoreCase("Property");
    }

    @Test
    void testGetDeedByTitle() {
        when(deedRepository.findByTitle("Property Transfer Agreement")).thenReturn(Optional.of(testDeed));

        Optional<Deed> result = deedService.getDeedByTitle("Property Transfer Agreement");

        assertTrue(result.isPresent());
        assertEquals("Property Transfer Agreement", result.get().getTitle());
        verify(deedRepository, times(1)).findByTitle("Property Transfer Agreement");
    }

    @Test
    void testGetDeedsByTypeAndTitle() {
        List<Deed> deeds = Arrays.asList(testDeed);
        when(deedRepository.findByTypeAndTitleContainingIgnoreCase(DeedType.SALE_DEED, "Property"))
                .thenReturn(deeds);

        List<Deed> result = deedService.getDeedsByTypeAndTitle(DeedType.SALE_DEED, "Property");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(DeedType.SALE_DEED, result.get(0).getType());
        verify(deedRepository, times(1)).findByTypeAndTitleContainingIgnoreCase(DeedType.SALE_DEED, "Property");
    }

}

