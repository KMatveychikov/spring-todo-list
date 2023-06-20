import React, {useContext, useState} from 'react';
import {Context} from "../index";
import classes from '../styles/Form.module.css';

const LoginForm = () => {
    const [email, setEmail] = useState<string>('')
    const [password, setPassword] = useState<string>('')
    const {store} = useContext(Context);
    return (
        <div className={classes.loginForm}>
            <input
                className={classes.login__items}
                onChange={e => setEmail(e.target.value)}
                value={email}
                type="text"
                placeholder='Email'
            />
            <input
                className={classes.login__items}
                onChange={e => setPassword(e.target.value)}
                value={password}
                type="password"
                placeholder='Пароль'
            />
            <button onClick={() => store.login(email, password)} className={classes.login__items}>
                Логин
            </button>
        </div>
    );
};

export default LoginForm;