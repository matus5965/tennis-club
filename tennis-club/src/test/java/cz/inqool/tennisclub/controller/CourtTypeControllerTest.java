package cz.inqool.tennisclub.controller;

import cz.inqool.tennisclub.dto.courttype.CourtTypeResponse;
import cz.inqool.tennisclub.dto.courttype.CourtTypeSaveRequest;
import cz.inqool.tennisclub.service.CourtTypeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;


import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CourtTypeController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CourtTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CourtTypeService service;

    @Test
    public void create_Success() throws Exception {
        CourtTypeSaveRequest request = new CourtTypeSaveRequest("Clay", BigDecimal.valueOf(10.0));
        CourtTypeResponse response = new CourtTypeResponse(1L, "Clay", BigDecimal.valueOf(10.0));

        when(service.create(any(CourtTypeSaveRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/court-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Clay"));
    }

    @Test
    public void getById_Success() throws Exception {
        CourtTypeResponse response = new CourtTypeResponse(1L, "Clay", BigDecimal.valueOf(10.0));

        when(service.getById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/court-types/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Clay"));
    }

    @Test
    public void getAll_Success() throws Exception {
        CourtTypeResponse response = new CourtTypeResponse(1L, "Clay", BigDecimal.valueOf(10.0));

        when(service.getAll()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/court-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Clay"));
    }

    @Test
    public void update_Success() throws Exception {
        CourtTypeSaveRequest request = new CourtTypeSaveRequest("Clay", BigDecimal.valueOf(12.0));
        CourtTypeResponse response = new CourtTypeResponse(1L, "Clay", BigDecimal.valueOf(12.0));

        when(service.update(eq(1L), any(CourtTypeSaveRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/court-types/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Clay"));
    }

    @Test
    public void delete_Success() throws Exception {
        doNothing().when(service).delete(1L);

        mockMvc.perform(delete("/api/court-types/1"))
                .andExpect(status().isNoContent());
    }
}