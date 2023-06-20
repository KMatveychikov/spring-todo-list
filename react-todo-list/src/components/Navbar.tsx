import React from 'react';
import {Link} from "react-router-dom";
import classes from '../styles/Navbar.module.css'
import {store} from "../index";
import {observer} from "mobx-react-lite";
import {Role} from "../models/Role";


const Navbar = observer(() => {
        const isAuth = store.isAuth
        if (isAuth) {
                return (
                    <div className={ classes.navbar }>
                        <div>
                            <Link to="/tasks" className={ classes.navbar__links }>Задачи</Link>

                        </div>
                    </div>
                );
        } else {
            return (
                <div className={ classes.navbar }>
                    <Link to="/login" className={ classes.navbar__links }>Войти</Link>
                    <Link to="/register" className={ classes.navbar__links }>Регистрация</Link>
                </div>
            );
        }
    }
);

export default Navbar;