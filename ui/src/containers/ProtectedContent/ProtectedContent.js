import React, {Component} from 'react'

import { Layout } from 'antd';


import Sidebar from '../Sidebar/Sidebar';
import PageContent from '../PageContent/PageContent';

class ProtectedContent extends Component {
  render() {
    return (
      <Layout>
        <Layout>  
          <Sidebar />
          <PageContent />
        </Layout>
      </Layout>
    )
  }
}

export default ProtectedContent
