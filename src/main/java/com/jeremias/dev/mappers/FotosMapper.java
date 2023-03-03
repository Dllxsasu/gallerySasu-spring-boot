package com.jeremias.dev.mappers;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.jeremias.dev.dtos.FotoDto;
import com.jeremias.dev.models.Foto;
import com.jeremias.dev.models.User;
import com.jeremias.dev.repository.UserRepository;
import com.jeremias.dev.security.services.AuthService;

@Mapper(componentModel = "spring")
public abstract class FotosMapper {
	@Autowired
    private UserRepository userRepository;

    /*
	Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "subreddit", source = "subreddit")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "description", source = "postRequest.description")
	 public abstract  Foto map(PostRequest postRequest, Subreddit subreddit, User user);
*/
    @Mapping(target = "fotoId", source = "id")
    @Mapping(target = "tags", source = "tags")
    @Mapping(target = "url", source = "url")
    @Mapping(target = "likeCount", expression = "java(obj.getLikes().intValue())")
    @Mapping(target = "views", expression = "java(obj.getViews().intValue())")

    @Mapping(target = "username", expression = "java(getUsername(obj.getUserId()))")
    public abstract FotoDto mapToDto(Foto obj); 
    
    
    public String getUsername(String userId) {
    	var user=  userRepository.findById(userId).orElseThrow();
    	return user.getUsername();
    }

    
}
