package com.grupo20.ttpsspringboot.controller;

import com.grupo20.ttpsspringboot.domain.models.Avistamiento;
import com.grupo20.ttpsspringboot.dtos.*;
import com.grupo20.ttpsspringboot.exceptions.NotFoundException;
import com.grupo20.ttpsspringboot.services.impl.AvistamientoService;
import com.grupo20.ttpsspringboot.services.IAvistamientoService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Avistamientos")
@RestController
@RequestMapping("/api/avistamientos")
public class AvistamientoController extends BaseController {

    @Autowired
    private IAvistamientoService service;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<AvistamientoDTO> create(@Parameter(
                                                          description = "Datos de la nueva publicación (JSON)",
                                                          content = @Content(
                                                                  mediaType = "application/json",
                                                                  schema = @Schema(implementation = AvistamientoCreateDTO.class)
                                                          )
                                                  )
                                                  @RequestPart("data") @Valid AvistamientoCreateDTO dto,
                                                  @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        try {
            Avistamiento avistamiento = service.create(getUsuario(), dto, files);
            return new ResponseEntity<>(AvistamientoDTO.fromEntity(avistamiento), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping()
    public ResponseEntity<List<AvistamientoDTO>> getFiltered(@ModelAttribute AvistamientoFilterDTO filter) {
        List<Avistamiento> avistamientos = service.getFiltered(filter);
        return ResponseEntity.ok(avistamientos.stream().map(AvistamientoDTO::fromEntity).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AvistamientoDTO> get(@PathVariable("id") Long id) {
        try {
            Avistamiento avistamiento = service.get(id);
            return new ResponseEntity<>(AvistamientoDTO.fromEntity(avistamiento), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<AvistamientoDTO> updateAvistamiento(
            @PathVariable Long id,
            @Parameter(
                    description = "Datos de actualización del avistamiento (JSON)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AvistamientoUpdateDTO.class)
                    )
            )
            @RequestPart("data") @Valid AvistamientoUpdateDTO dto,

            @RequestPart(value = "files", required = false) List<MultipartFile> files) {

        try {
            AvistamientoDTO updatedAvistamiento = service.update(id, dto, files); // <-- Llama al servicio
            return new ResponseEntity<>(updatedAvistamiento, HttpStatus.OK);

        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
