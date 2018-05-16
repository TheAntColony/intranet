import React, {Component} from 'react'
import UserService from '../../services/User'
import {
  Link
} from 'react-router-dom';

import { Table, Divider } from 'antd';

const columns = [{
  title: 'Id',
  dataIndex: 'id',
  key: 'id', 
  width: 80,
  fixed: 'left'
},{
  title: 'First Name',
  dataIndex: 'firstName',
  width: 100
}, {
  title: 'Last Name',
  dataIndex: 'lastName',
  key: 'lastName',
  width: 100
}, {
  title: 'Email',
  dataIndex: 'email',
  key: 'email',
  width: 100
}, {
  title: 'Action',
  key: 'operation',
  width: 180,
  render: (record) => {
      var userId = record.id

      return <span>
        <Link to={ `/users/${userId}/view` }>
          View
        </Link>
        <Divider type='vertical' />
        <Link to={ `/users/${userId}/edit` }>
          Edit
        </Link>
        <Divider type='vertical' />
        <Link to={ `/users/${userId}/delete` }>
          Delete
        </Link>
      </span>
  }
}];

class UserList extends Component {
  constructor(props) {
    super(props)
    this.state = { 
      data: [],
      pageSize: 10,
      pagination: {
        pageSizeOptions: [ '10', '20', '50'],
        showSizeChanger: true,
        onShowSizeChange: this.onShowSizeChange.bind(this),
      },
      loading: false,
    }
  }

  onShowSizeChange(current, pageSize) {
    const pager = { ...this.state.pagination };
    pager.pageSize = pageSize
    this.setState({
      pagination: pager,
      pageSize: pageSize
    })
  }

  handleTableChange = (pagination, filters, sorter) => {
    const pager = { ...this.state.pagination };
    pager.current = pagination.current;
    this.setState({
      pagination: pager,
    });

    this.fetch();
  }

  getOffset(page) {
    var offset = page ? ((page - 1) * this.state.pageSize) : 0
    return offset
  }

  fetch = (params = {}) => {
    this.setState({ loading: true });
    UserService.getTotal()
      .then((response) => {
        const pagination = { ...this.state.pagination };
        pagination.total = response;
        var offset = this.getOffset(this.state.pagination.current)
        return UserService.getWithOffsetAndLimit(offset, this.state.pageSize)
          .then((users) => {
            this.setState({ 
              loading: false,
              data: users.data,
              pagination
            });
          })
      })
      .catch((e) => {
        console.log(e)
      })
  }

  async componentDidMount() {
    this.fetch()
  }

  render() {
    return (
      <Table rowKey='id' 
        dataSource={this.state.data} 
        columns={columns} 
        pagination={this.state.pagination}
        loading={this.state.loading}
        onChange={this.handleTableChange.bind(this)}
        scroll={{ x: 800 }} />
    )
  }
}

export default UserList
