package com.jeremias.dev.service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jeremias.dev.GalleryPhotosBackApplication;
import com.jeremias.dev.constantes.PhotoStatus;

import com.jeremias.dev.dtos.FotoDto;
import com.jeremias.dev.dtos.UploadFotoResponse;

import com.jeremias.dev.exception.GalleryPhotosException;


import com.jeremias.dev.mappers.FotosMapper;

import com.jeremias.dev.models.Foto;
import com.jeremias.dev.repository.FotoRepository;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FotoService {
    private final FotoRepository fotoRepository;
    private final S3Service s3Service;
    private final UserService userService;
    private final FotosMapper fotoMapper;
    
    public UploadFotoResponse uploadVideo(MultipartFile file, String userId) {
    	//Subimos el file a s3 y nos devolvera una URI
        String url = s3Service.upload(file);
        Foto foto = new Foto();
        //Seteamos la URL
        foto.setUrl(url);
        //fail-fast
        Objects.requireNonNull(userId);
        
        foto.setUserId(userId);
        fotoRepository.save(foto);
        //devolvemos el nuevo video subido con su URL
        return new UploadFotoResponse(foto.getId(), url);
    }
    
    public List<FotoDto> getALLFotosByCUENTA(String userId) {
    	
        List<Foto> fotos = fotoRepository.findByUserId(userId);
        //buscamos por Usuario  y obtemos todo, de ahi lo hacemos stream, convertimos a Dto y devolvemos la lista
        return fotos.stream()
                .map(fotoMapper::mapToDto)
                .collect(Collectors.toList());
        
    }
    public FotoDto editFotoDetails(FotoDto fotoDto) {
        var foto = getFotoById(fotoDto.getFotoId());
        //Seteamos los campos que deseamos cambiar
        foto.setTitulo(fotoDto.getTitulo());
        
        foto.setUrl(fotoDto.getUrl());
        // Ignore Channel ID as it should not be possible to change the Channel of a Video
        foto.setTags(fotoDto.getTags());
        foto.setStatus(fotoDto.getPhotoStatus());
        //Ingnoramos los campos que no debemos cambiar como son las vistas,likes,etc.
        // View Count is also ignored as its calculated independently
        fotoRepository.save(foto);
        //map to Dto 
        return fotoMapper.mapToDto(foto);
    }

    public void deleteFoto(String id) {
    	//Obtemos la url Del video
        String videoUrl = getFotoById(id).getUrl();
        //Eliminamos el video de s3 
        s3Service.deleteFile(videoUrl);
    }

    
    
    public List<FotoDto> getAllFotos() {
    	//Obtemos todos los videos
        return fotoRepository.findAll()
                .stream()
                //Filtramos que solo sean videos publicos
                .filter(item -> PhotoStatus.PUBLIC.equals(item.getStatus()))
                .map(fotoMapper::mapToDto)
                .collect(Collectors.toList());
    }
    
    
    public FotoDto getFoto(String id) {
        var videoDto = fotoMapper.mapToDto(getFotoById(id));
        // This method is called when the Get Video Metadata API is called, which is usually called when user clicks on
        // a video, hence we will increase the view count of the video.
        increaseViewCount(videoDto);
        return videoDto;
    }
    private void increaseViewCount(FotoDto videoDto) {
        var foto = getFotoById(videoDto.getFotoId());
        //Incrementamos el numero de vista con atomicInter
        //The primary use of AtomicInteger is when you are in a multithreaded context 
        //and you need to perform thread safe operations on an integer without using synchronized
        //en este caso nosotros lo utilizamos porque hay la posibilidad de que varios usuarios al mismo tiempo vean el video
        foto.increaseViewCount();
        fotoRepository.save(foto);
    }

    private Foto getFotoById(String id) {
        return fotoRepository.findById(id)
                .orElseThrow(() -> new GalleryPhotosException("No se pudo encontrar la foto ocn el ID - " + id));
    }
    public FotoDto like(String fotoId) {
        var foto = getFotoById(fotoId);
        //verificamos si le dio like y removemos dislike o like o en ultimo caso aumentamos el lke
        if (userService.ifLikedVideo(fotoId)) {
        	foto.decreaseLikeCount();
            userService.removeFromLikedVideos(fotoId);
        } else {
        	foto.increaseLikeCount();
            userService.addLikedFotos(fotoId);
        }
        fotoRepository.save(foto);
        return fotoMapper.mapToDto(foto);
    }

   

  
}
