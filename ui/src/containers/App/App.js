import React, {Component} from 'react'
import {
  BrowserRouter as Router
} from 'react-router-dom'

import { Layout } from 'antd'

import PageHeader from '../PageHeader/Header'

import MainRoutes from '../../routes/Main'

import './App.css';
import 'antd/dist/antd.css';

class App extends Component {
  render() {
    return (
      <Router>
        <Layout style={{ minHeight: '100vh' }}>
        <PageHeader />

        <MainRoutes />
      </Layout>
      </Router>
    );
  }
}

export default App;
