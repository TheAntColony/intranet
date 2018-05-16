import React, {Component} from 'react'
import { Link } from 'react-router-dom'
import { Calendar, Alert, Button, Row, Col, Badge, Popover } from 'antd';
import moment from 'moment';

import Leave from '../../services/Leave'

import './LeaveCalendar.css'

const BadgeStatus = {
  Success: 'success',
  Processing: 'processing',
  Default: 'default',
  Error: 'error',
  Warning: 'warning'
}

var leaveToBadgeStatusMap = {}
leaveToBadgeStatusMap[Leave.status.Approved] = BadgeStatus.Success
leaveToBadgeStatusMap[Leave.status.Pending] = BadgeStatus.Processing
leaveToBadgeStatusMap[Leave.status.Rejected] = BadgeStatus.Error

class LeaveCalendar extends Component {
  constructor(props) {
    super(props)

    this.dateCellRender = this.dateCellRender.bind(this)
    this.onPanelChange = this.onPanelChange.bind(this)
  }

  state = {
    value: moment(),
    selectedValue: moment(),
    pendingCount: 0,
    leaves: []
  }


  onSelect = (value) => {
    console.log('Selected', value)
    this.setState({
      value,
      selectedValue: value,
    });
  }

  onPanelChange = (value) => {
    this.setState({ value });
  }

  disabledDate = (date) => {
    return false;
  }

  dateCellRender(value) {
    /* Filter leaves that are on specific date passed to function when calendar 
    is rendered to show details about leaves on that date */
    var leaves = this.state.leaves.filter((leave) => {
        var isDateInRange = value.isBetween(leave.startDate, leave.endDate, 'day', '[]')
        // console.log(leave, value, isDateInRange)
        return isDateInRange
    })

    const popOverContent = (
      <div>
        {
          leaves.map(leave => (
            <div key={leave.id}>
              <span>{leave.user.firstName} </span>
              <span>{leave.user.lastName} </span>
              <span>{leave.status}</span>
            </div>
          ))
        }
      </div>
    );

    return (
      <Popover content={popOverContent} title={`Leaves for ${value.format('DD/MM/YYYY')}`} trigger='hover'>
        <ul className='events'>
          {
            leaves.map(leave => (
              <li key={leave.id}>
                <Badge status={leaveToBadgeStatusMap[leave.status]} text={leave.user.email} />
              </li>
            ))
          }
        </ul>
      </Popover>
    );
  }

  getPendingCount() {
    Leave.getTotalPending()
      .then((count) => {
        this.setState({ pendingCount: count})
      })
  }

  getLeaves() {
    Leave.get()
      .then((response) => {
        console.log(response)
        this.setState({ leaves: response.data })
      })
  }

  componentDidMount() {
    this.getPendingCount()
    this.getLeaves()
  }

  render() {
    const { value } = this.state;
    return (
      <Row>
        <Row className='totalLeave'>
          <Alert message={`There are total ${this.state.pendingCount} leaves pending evaluation`} type='info' />
        </Row>  
        <Row className='flexCenter' gutter={16}>
          <Col>
            <Button className='requestLeaveBtn' type='primary'>
              <Link to='/leaves/request'>Request leave</Link>
            </Button>
          </Col>
          <Col>
            <Button type='primary'>
              <Link to='/leaves/evaluate'>Evaluate leaves</Link>
            </Button>
          </Col>
        </Row>
        <Row className='calendarRow'>
          <Calendar value={value} 
            onSelect={this.onSelect} 
            onPanelChange={this.onPanelChange} 
            dateCellRender={this.dateCellRender} 
            disabledDate={this.disabledDate} />
        </Row>
      </Row>
    );
  }
}

export default LeaveCalendar 
