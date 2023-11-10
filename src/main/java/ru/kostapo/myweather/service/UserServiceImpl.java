package ru.kostapo.myweather.service;

import ru.kostapo.myweather.model.dto.UserResDto;
import ru.kostapo.myweather.exception.PasswordMismatchException;
import ru.kostapo.myweather.exception.UserNotFoundException;
import ru.kostapo.myweather.exception.ValidConstraintViolationException;
import ru.kostapo.myweather.model.Session;
import ru.kostapo.myweather.repository.UserRepository;
import ru.kostapo.myweather.model.dto.UserReqDto;
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
    public UserResDto userRegistration (UserReqDto userReqDto) {
        User user = UserMapper.INSTANCE.toModel(userReqDto);
        if(isUserValid(user)) {
            user.setPassword(PasswordUtil.hashPassword(userReqDto.getPassword()));
        }
        Session session = sessionService.newSessionForUser(user);
        user.getSessions().add(session);
        User persistUser = userRepository.save(user);
        return UserMapper.INSTANCE.toDto(persistUser);
    }

    @Override
    public UserResDto userLogin(UserReqDto userReqDto) {
        Optional<User> user = userRepository.findByKey(userReqDto.getLogin());
        if(user.isPresent()) {
            if(PasswordUtil.checkPassword(userReqDto.getPassword(), user.get().getPassword())) {
                Session newSession = sessionService.newSessionForUser(user.get());
                sessionService.save(newSession);
                user.get().getSessions().add(newSession);
                return UserMapper.INSTANCE.toDto(user.get());
            }
            throw new PasswordMismatchException("Не верный пароль");
        }
        throw new UserNotFoundException("Не верный логин");
    }

    @Override
    public UserResDto findByKey(String login) {
        Optional<User> user = userRepository.findByKey(login);
        if(user.isPresent()) {
            return UserMapper.INSTANCE.toDto(user.get());
        }
        throw new UserNotFoundException(String.format("%s - логин не найден", login));
    }

    private boolean isUserValid(User user) {
        Set<ConstraintViolation<User>> violations = ValidationUtil.getValidator().validate(user);
        if(!violations.isEmpty()) {
            throw new ValidConstraintViolationException("НАРУШЕНИЕ ОГРАНИЧЕНИЙ ВАЛИДАЦИИ", violations);
        }
        return true;
    }
}
