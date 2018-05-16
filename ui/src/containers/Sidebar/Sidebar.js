import React , {Component} from 'react'
import { Layout, Menu, Icon } from 'antd';

import {
  Link
} from "react-router-dom";

import './Sidebar.css';
import 'antd/dist/antd.css';

const { Sider } = Layout;

class Sidebar extends Component {
  urlToSelectedMenuMapping = {
    '/users': '15',
    '/leaves': '16',
    '/projects': '17'
  }

  state = {
    collapsed: false
  };

  getSelectedMenuKeys() {
    return Object.keys(this.urlToSelectedMenuMapping)
      .filter((prop) => {
        if(~window.location.pathname.indexOf(prop)) {
          return this.urlToSelectedMenuMapping[prop];
        } 

        return null;
    }).map(key => this.urlToSelectedMenuMapping[key])
  }

  componentDidMount() {
    var selectedMenuKeys = this.getSelectedMenuKeys()
    this.setState({ selectedMenuKeys : selectedMenuKeys.length > 0 ? selectedMenuKeys : [this.urlToSelectedMenuMapping['/users']] })
  }

  onCollapse = (collapsed) => {
    this.setState({ collapsed });
  }

  menuClick = (item) => {
    this.setState({ selectedMenuKeys: item.keyPath })
  }

  render() {
    return (
      <Sider
          breakpoint="md"
          collapsedWidth="0"
          collapsible
          collapsed={this.state.collapsed}
          onCollapse={this.onCollapse}
        >
          <Menu theme="dark" 
            onClick={this.menuClick} 
            selectedKeys={ this.state.selectedMenuKeys } 
            mode="inline">
            <Menu.Item key="15">
                <Link to="/users">            
                  <Icon type="user" /> 
                  <span>Users</span>
                </Link>
            </Menu.Item>
            <Menu.Item key="16">
              <Link to="/leaves">            
                <Icon type="calendar" />
                <span>Leaves</span>
              </Link>
            </Menu.Item>
            <Menu.Item key="17">
              <Link to="/projects">
                <Icon type="code" />
                <span>Projects</span>
              </Link>
            </Menu.Item>
          </Menu>
        </Sider>
    )
  }
}

export default Sidebar
