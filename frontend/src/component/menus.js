import { NavLink } from 'react-router-dom'
import React from 'react';

const selectedStyle = {
    backgroundColor: "white",
    color: "slategray"
};

export const MainMenu = () =>
    <nav className="main-menu">
        <NavLink to="/about" activeStyle={selectedStyle}>[About]</NavLink>
        <NavLink to="/events" activeStyle={selectedStyle}>[Events]</NavLink>
        <NavLink to="/products" activeStyle={selectedStyle}>[Products]</NavLink>
        <NavLink to="/contact" activeStyle={selectedStyle}>[Contact Us]</NavLink>
    </nav>;

export const AboutMenu = ({ match }) =>
    <div className="about-menu">
        <li>
            <NavLink to="/about"
                     style={selectedStyle}>
                [Company]
            </NavLink>
        </li>
        <li>
            <NavLink to="/about/history"
                     activeStyle={selectedStyle}>
                [History]
            </NavLink>
        </li>
        <li>
            <NavLink to="/about/services"
                     activeStyle={selectedStyle}>
                [Services]
            </NavLink>
        </li>
        <li>
            <NavLink to="/about/location"
                     activeStyle={selectedStyle}>
                [Location]
            </NavLink>
        </li>
    </div>;
