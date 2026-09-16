package cz.inqool.tennisclub.controller;

import cz.inqool.tennisclub.exception.NotFoundException;
import tools.jackson.databind.ObjectMapper;
import cz.inqool.tennisclub.dto.court.CourtResponse;
import cz.inqool.tennisclub.dto.court.CourtSaveRequest;
import cz.inqool.tennisclub.service.CourtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CourtController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CourtControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CourtService service;

    @Test
    public void create_Success() throws Exception {
        CourtSaveRequest request = new CourtSaveRequest("Court 1", 1L);
        CourtResponse response = new CourtResponse(1L, "Court 1", null, null);

        when(service.create(any(CourtSaveRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/courts")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(service).create(any(CourtSaveRequest.class));
    }

    @Test
    public void getById_Success() throws Exception {
        CourtResponse response = new CourtResponse(1L, "Court-1", null, null);

        when(service.getById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/courts/1"))
                .andExpect(status().isOk());

        verify(service).getById(1L);
    }

    @Test
    public void getById_WhenUnknownId_NotFound() throws Exception {
        when(service.getById(99L)).thenThrow(new NotFoundException("Court not found: 99"));

        mockMvc.perform(get("/api/courts/99"))
                .andExpect(status().isNotFound());

        verify(service).getById(99L);
    }

    @Test
    public void getAll_Success() throws Exception {
        when(service.getAll()).thenReturn(List.of(
                new CourtResponse(1L, "Court 1", null, null),
                new CourtResponse(2L, "Court 2", null, null)
        ));

        mockMvc.perform(get("/api/courts"))
                .andExpect(status().isOk());

        verify(service).getAll();
    }

    @Test
    public void update_Success() throws Exception {
        CourtSaveRequest request = new CourtSaveRequest("Court 1 Updated", 1L);
        CourtResponse response = new CourtResponse(1L, "Court 1 Updated", null, null);

        when(service.update(eq(1L), any(CourtSaveRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/courts/1")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(service).update(eq(1L), any(CourtSaveRequest.class));
    }

    @Test
    public void delete_Success() throws Exception {
        doNothing().when(service).delete(1L);

        mockMvc.perform(delete("/api/courts/1"))
                .andExpect(status().isNoContent());

        verify(service).delete(1L);
    }
}