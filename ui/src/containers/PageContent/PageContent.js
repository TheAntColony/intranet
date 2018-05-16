import React, {Component} from 'react'
import { Layout } from 'antd'

import ProtectedRoutes from '../../routes/Protected'

const { Content } = Layout;
const { Footer } = Layout;

class PageContent extends Component {
  constructor(props) {
    super(props);
    this.state = { users: [] };
  }

  render() {
    return (
      <Layout style={{ padding: '0 24px 24px' }}>
        <div style={{ 'marginTop': '10px' }}></div>
        <Content style={{ background: '#fff', padding: 24, margin: 0, minHeight: 280 }}>
          <ProtectedRoutes />
        </Content>
        <Footer style={{ textAlign: 'center' }}>
          Ant Colony ©2018 Created by Ant Dzej
        </Footer>
      </Layout>)
  }
}

export default PageContent
