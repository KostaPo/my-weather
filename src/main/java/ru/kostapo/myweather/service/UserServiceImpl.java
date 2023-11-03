package ru.kostapo.myweather.service;

import ru.kostapo.myweather.dto.UserResDto;
import ru.kostapo.myweather.exception.PasswordMismatchException;
import ru.kostapo.myweather.exception.UserNotFoundException;
import ru.kostapo.myweather.exception.ValidConstraintViolationException;
import ru.kostapo.myweather.model.Session;
import ru.kostapo.myweather.repository.SessionRepository;
import ru.kostapo.myweather.repository.UserRepository;
import ru.kostapo.myweather.dto.UserReqDto;
import ru.kostapo.myweather.model.mapper.UserMapper;
import ru.kostapo.myweather.model.User;
import ru.kostapo.myweather.utils.PasswordUtil;
import ru.kostapo.myweather.utils.ValidationUtil;

import javax.validation.ConstraintViolation;
import java.util.Optional;
import java.util.Set;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final SessionService sessionService;

    public UserServiceImpl() {
        this.userRepository = new UserRepository();
        this.sessionService = new SessionServiceImpl();
    }

    @Override
    public UserResDto save (UserReqDto userReqDto) {
        User user = UserMapper.INSTANCE.toModel(userReqDto);
        Set<ConstraintViolation<User>> violations = ValidationUtil.getValidator().validate(user);
        if(!violations.isEmpty()) {
            throw new ValidConstraintViolationException("НАРУШЕНИЕ ОГРАНИЧЕНИЙ ВАЛИДАЦИИ", violations);
        }
        user.setPassword(PasswordUtil.hashPassword(userReqDto.getPassword()));
        User persistUser = userRepository.save(user);
        Session session = sessionService.getNewSession(user);
        user.setSession(session);
        return UserMapper.INSTANCE.toDto(persistUser);
    }

    @Override
    public UserResDto authentication(UserReqDto userReqDto) {
        Optional<User> user = userRepository.findByKey(userReqDto.getLogin());
        if(user.isPresent()) {
            if(PasswordUtil.checkPassword(userReqDto.getPassword(), user.get().getPassword())) {
                return UserMapper.INSTANCE.toDto(user.get());
            }
            throw new PasswordMismatchException("Не верный пароль");
        }
        throw new UserNotFoundException("Не верный логин");
    }
}
