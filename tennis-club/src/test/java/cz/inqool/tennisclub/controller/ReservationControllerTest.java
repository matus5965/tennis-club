package cz.inqool.tennisclub.controller;

import cz.inqool.tennisclub.exception.BadRequestException;
import cz.inqool.tennisclub.exception.ConflictException;
import tools.jackson.databind.ObjectMapper;
import cz.inqool.tennisclub.dto.reservation.ReservationCreateRequest;
import cz.inqool.tennisclub.dto.reservation.ReservationResponse;
import cz.inqool.tennisclub.dto.reservation.ReservationUpdateRequest;
import cz.inqool.tennisclub.entity.GameType;
import cz.inqool.tennisclub.service.ReservationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReservationController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ReservationService service;

    @Test
    public void create_Success() throws Exception {
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = start.plusHours(1);
        ReservationCreateRequest request = new ReservationCreateRequest(
                "Court 1", "+421000000000", "John Doe",
                start, end, GameType.SINGLES);
        ReservationResponse response = new ReservationResponse(
                1L, null, null, null,
                start, end, GameType.SINGLES, BigDecimal.valueOf(120.0), null);

        when(service.create(any(ReservationCreateRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/reservations")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(service).create(any(ReservationCreateRequest.class));
    }

    @Test
    public void create_WhenOverlapping_BadRequest() throws Exception {
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = start.plusHours(1);
        ReservationCreateRequest request = new ReservationCreateRequest(
                "Court 1", "+421000000000", "John Doe",
                start, end, GameType.SINGLES);

        when(service.create(any(ReservationCreateRequest.class)))
                .thenThrow(new ConflictException(""));

        mockMvc.perform(post("/api/reservations")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(service).create(any(ReservationCreateRequest.class));
    }

    @Test
    public void create_WhenInvalidTimes_BadRequest() throws Exception {
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = start.minusMinutes(30);
        ReservationCreateRequest request = new ReservationCreateRequest(
                "Court 1", "+421000000000", "John Doe",
                start, end, GameType.SINGLES);

        when(service.create(any(ReservationCreateRequest.class)))
                .thenThrow(new BadRequestException(""));

        mockMvc.perform(post("/api/reservations")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(service).create(any(ReservationCreateRequest.class));
    }

    @Test
    public void update_Success() throws Exception {
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = start.plusHours(1);
        ReservationUpdateRequest request = new ReservationUpdateRequest(start, end, GameType.DOUBLES);
        ReservationResponse response = new ReservationResponse(
                1L, null, null, null,
                start, end, GameType.DOUBLES, BigDecimal.valueOf(180.0), null);

        when(service.update(eq(1L), any(ReservationUpdateRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/reservations/1")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(service).update(eq(1L), any(ReservationUpdateRequest.class));
    }

    @Test
    public void delete_Success() throws Exception {
        doNothing().when(service).delete(1L);

        mockMvc.perform(delete("/api/reservations/1"))
                .andExpect(status().isNoContent());

        verify(service).delete(1L);
    }

    @Test
    public void getAll_Success() throws Exception {
        when(service.getAll()).thenReturn(List.of(new ReservationResponse(
                1L, null, null, null,
                null, null, null, null, null)));

        mockMvc.perform(get("/api/reservations"))
                .andExpect(status().isOk());

        verify(service).getAll();
    }

    @Test
    public void getByCourtNumber_Success() throws Exception {
        when(service.getByCourtNumber("Court1"))
                .thenReturn(List.of(new ReservationResponse(
                        1L, null, null, null,
                null, null, null, null, null)));

        mockMvc.perform(get("/api/reservations/court/Court1"))
                .andExpect(status().isOk());

        verify(service).getByCourtNumber("Court1");
    }

    @Test
    public void getByPhoneNumber_Success() throws Exception {
        when(service.getByPhoneNumber("+421000000000", true))
                .thenReturn(List.of(new ReservationResponse(
                        1L, null, null, null,
                        null, null, null, null, null)));

        mockMvc.perform(get("/api/reservations/customer/+421000000000")
                        .param("futureOnly", "true"))
                .andExpect(status().isOk());

        verify(service).getByPhoneNumber("+421000000000", true);
    }
}