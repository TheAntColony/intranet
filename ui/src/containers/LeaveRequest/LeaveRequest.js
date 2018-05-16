import React, {Component} from 'react'
import { message, DatePicker, Input, Button } from 'antd'
import moment from 'moment'

import LeaveCategory from '../../services/LeaveCategory'
import Leave from '../../services/Leave'
import DynamicSelect from '../../components/DynamicSelect'

import './LeaveRequest.css'
import LocalStorage from '../../utils/LocalStorageUtil';
import UserAuth from '../../services/UserAuth';

const { RangePicker } = DatePicker

class LeaveRequest extends Component {
  constructor(props) {
    super(props)

    this.handleSubmit = this.handleSubmit.bind(this)
    this.onCalendarChange = this.onCalendarChange.bind(this)
    this.handleTypeChange = this.handleTypeChange.bind(this)
    this.reasonChange = this.reasonChange.bind(this)
  }

  state = {
    size: 'default',
    categories: [],
    request: {
      category: {
        id: 0,
        name: ''
      },
      user: {
        id: 0,
        firstName: '',
        lastName: '',
        email: ''
      },
      reason: '',
      startDate: '',
      endDate:''
    }
  }

  disabledDate(current) {
    // Can not select days before today and today
    return current && current < moment().endOf('day');
  }

  onCalendarChange(value) {
    if(value.length === 2) {
      var request = {...this.state.request}
      request.startDate = value[0]
      request.endDate = value[1]
      this.setState({ request })
    }
  }

  handleTypeChange(categoryTypeId) {
    var request = {...this.state.request}
    request.category.id = categoryTypeId;
    request.user = LocalStorage.getItem(UserAuth.userInfoKey)
    this.setState({request})
  }

  reasonChange(event) {
    var request = {...this.state.request}
    request.reason = event.target.value
    this.setState({ request })
  }

  checkSubmit(obj) {
    for (var key in obj) {
      if (obj[key] === null || obj[key] === '') {
        console.log(key + obj[key])
        return false;
      }
          
    }

    return true;
  }

  handleSubmit(event) {
    var isValid = this.checkSubmit(this.state.request)
    if(isValid) {
      Leave.post(this.state.request)
        .then((response) => {
          console.log(response)
          message.success('Leave successfully requested')
          setTimeout(() => window.location.href = '/leaves' , 2000)
        })
        .catch((err) => {
          message.err(err.response)
        })
    } else {
      message.error('Please fill all fields');
    }
  }


  componentDidMount() {
    LeaveCategory.get()
      .then((response) => {
        console.log(response)
        this.setState({ categories: response.data })
      })
  }

  render() {

    return (
      <form>
          <div className='divField'>
            <span className='fieldDescription'>Pick date</span> 
            <RangePicker size={this.state.size} 
              onCalendarChange={this.onCalendarChange.bind(this)}
              disabledDate={this.disabledDate.bind(this)} />
          </div>
          <div className='divField'>
            <span className='fieldDescription'>Type</span> 
            <DynamicSelect data={this.state.categories}  handleChange={this.handleTypeChange.bind(this)}/>
          </div>
          <div className='divField'>
            <span className='fieldDescription'>Reason</span> 
            <Input className='reason' placeholder='Enter leave reason' value={this.state.request.reason} onChange={this.reasonChange.bind(this)} required />
          </div>
          <div className='requestBtnDiv'>
            <Button className='requestBtn' type='primary' onClick={this.handleSubmit}>Request</Button>
          </div>
      </form>

    );
  }
}

export default LeaveRequest 
/*
      
*/
