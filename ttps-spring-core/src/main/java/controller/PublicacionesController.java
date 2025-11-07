package controller;


import domain.models.Publicacion;
import dtos.PublicacionCreateDTO;
import dtos.PublicacionDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.impl.PublicacionService;

@RestController
@RequestMapping("/api/publicaciones")
public class PublicacionesController extends BaseController {

    @Autowired
    private PublicacionService publicacionService;

    @PostMapping()
    public ResponseEntity<PublicacionDTO> createPublicacion(@Valid @RequestBody PublicacionCreateDTO dto) {
        try {
            Publicacion publicacion = publicacionService.create(getUsuario(), dto);
            return new ResponseEntity<>(PublicacionDTO.fromEntity(publicacion), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}