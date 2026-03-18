package com.back.site.answer.service;

import com.back.DataNotFoundException;
import com.back.site.answer.entity.Answer;
import com.back.site.answer.repository.AnswerRepository;
import com.back.site.question.entitiy.Question;
import com.back.site.user.entitiy.SiteUser;
import com.back.site.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final UserService userService;

    public Answer create(Question question, String content, String username) {
        SiteUser author = userService.getUser(username);
        Answer answer = new Answer(question, content, author);
        answerRepository.save(answer);
        return answer;
    }

    public List<Answer> findAll(){
        return answerRepository.findAll();
    }

    public Answer getAnswer(Integer id) {
        Optional<Answer> answer = answerRepository.findById(id);
        if (answer.isPresent()) {
            return answer.get();
        } else {
            throw new DataNotFoundException("answer not found");
        }
    }
    @Transactional
    public void modify(Answer answer, String content){
        answer.update( content);
    }
    @Transactional
    public void delete(Answer answer){
        answerRepository.delete(answer);
    }
    @Transactional
    public void vote(Answer answer, SiteUser siteUser) {
        answer.getVoter().add(siteUser);
        answerRepository.save(answer);
    }
}
