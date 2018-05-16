import React, {Component} from 'react'
import { Link } from 'react-router-dom'
import { Layout } from 'antd';

import AuthButton from '../AuthButton/AuthButton'

import './Header.css'

const { Header } = Layout;

class PageHeader extends Component {
  state = {
    collapsed: false,
  }

  toggle = () => {
    this.setState({
      collapsed: !this.state.collapsed,
    })
  }

  render() {
    return (
      <Header className="header">
        <Link to='/'>
          <div className="logo" />
        </Link>
        <AuthButton />
      </Header>
    )
  }
}

export default PageHeader;
