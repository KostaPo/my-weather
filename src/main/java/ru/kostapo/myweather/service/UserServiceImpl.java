package ru.kostapo.myweather.service;

import ru.kostapo.myweather.dto.UserResDto;
import ru.kostapo.myweather.exception.PasswordMismatchException;
import ru.kostapo.myweather.exception.UserNotFoundException;
import ru.kostapo.myweather.exception.ValidConstraintViolationException;
import ru.kostapo.myweather.model.Session;
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
        Optional<User> tmpUsr = userRepository.findByKey(userReqDto.getLogin());
        if(tmpUsr.isPresent()) {
            if(PasswordUtil.checkPassword(userReqDto.getPassword(), tmpUsr.get().getPassword())) {
                Session newSession = sessionService.newSessionForUser(tmpUsr.get());
                sessionService.save(newSession);
                tmpUsr.get().getSessions().add(newSession);
                return UserMapper.INSTANCE.toDto(tmpUsr.get());
            }
            throw new PasswordMismatchException("Не верный пароль");
        }
        throw new UserNotFoundException("Не верный логин");
    }

    private boolean isUserValid(User user) {
        Set<ConstraintViolation<User>> violations = ValidationUtil.getValidator().validate(user);
        if(!violations.isEmpty()) {
            throw new ValidConstraintViolationException("НАРУШЕНИЕ ОГРАНИЧЕНИЙ ВАЛИДАЦИИ", violations);
        }
        return true;
    }
}
