package com.kztp.testengine.service;

import com.kztp.testengine.exception.UnauthorizedRequestException;
import com.kztp.testengine.model.EditNews;
import com.kztp.testengine.model.NewNews;
import com.kztp.testengine.model.News;
import com.kztp.testengine.model.User;
import com.kztp.testengine.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public final class NewsService {
    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private UserService userService;


    public Page<News> getAllNews(Pageable pageable){
        return newsRepository.findAll(pageable);
    }

    public void createNews(NewNews news) throws UnauthorizedRequestException {
        User user = userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if(!user.getAuthorities().get(0).equals("ROLE_ADMIN")){
            throw new UnauthorizedRequestException("Access denied");
        }
        News createdNews = new News();
        createdNews.setAuthor(user);
        createdNews.setContent(news.getContent());
        createdNews.setTitle(news.getTitle());
        newsRepository.save(createdNews);

    }

    public void deleteNews(Integer newsId) throws UnauthorizedRequestException {
        User user = userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if(!user.getAuthorities().get(0).equals("ROLE_ADMIN")){
            throw new UnauthorizedRequestException("Access denied");
        }
        newsRepository.delete(newsRepository.findById(newsId).get());
    }

    public void editNews(EditNews news) throws UnauthorizedRequestException {
        User user = userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if(!user.getAuthorities().get(0).equals("ROLE_ADMIN")){
            throw new UnauthorizedRequestException("Access denied");
        }
        News originalNews = newsRepository.findById(news.getId()).get();
        originalNews.setTitle(news.getTitle());
        originalNews.setContent(news.getContent());
        newsRepository.save(originalNews);

    }
}
