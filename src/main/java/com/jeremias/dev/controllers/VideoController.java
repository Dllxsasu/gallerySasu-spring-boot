package com.jeremias.dev.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import com.jeremias.dev.dtos.FotoDto;
import com.jeremias.dev.dtos.UploadFotoResponse;
import com.jeremias.dev.service.FotoService;

import lombok.AllArgsConstructor;

@RequestMapping("/fotos")
@RestController
@AllArgsConstructor
public class VideoController {
	private final FotoService fotoService;

    @PostMapping("upload")
    public ResponseEntity<UploadFotoResponse> uploadVideo(@RequestParam("file") MultipartFile file,
                                                           @RequestParam("userId") String userId,
                                                           UriComponentsBuilder uriComponentsBuilder) {
        UploadFotoResponse videoResponse = fotoService.uploadVideo(file, userId);
        var uriComponents = uriComponentsBuilder.path("/{id}").buildAndExpand(videoResponse.getVideoId());
        return ResponseEntity.created(uriComponents.toUri())
                .body(videoResponse);
    }

    

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public FotoDto editVideoMetadata(@RequestBody @Validated FotoDto videoMetaDataDto) {
        return fotoService.editFotoDetails(videoMetaDataDto);
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public FotoDto getVideoMetaData(@PathVariable String id) {
        return fotoService.getFoto(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<FotoDto> getVideoMetaData() {
        return fotoService.getAllFotos();
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVideo(@PathVariable String id) {
        fotoService.deleteFoto(id);
    }

    @GetMapping("channel/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<FotoDto> allChannelVideos(@PathVariable String userId) {
        return fotoService.getALLFotosByCUENTA(userId);
    }

    @PostMapping("{id}/like")
    @ResponseStatus(HttpStatus.OK)
    public FotoDto likeVideo(@PathVariable String id) {
        return fotoService.like(id);
    }

  
   

   
}
