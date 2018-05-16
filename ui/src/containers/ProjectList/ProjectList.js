import React, {Component} from 'react'
import moment from 'moment'
import {Link} from 'react-router-dom'
import {Row, Col, Button} from 'antd'

import Project from '../../services/Project'

import ProjectCol from '../../components/ProjectCol'

import './ProjectList.css'

class ProjectList extends Component {
  state = {
    projects: []
  }

  componentDidMount() {
    Project.get()
      .then((response) => {
        console.log(response.data)
        this.setState({ projects: response.data })
      })
  }

  getDateFormat(date) {
    return moment(date).format('DD-MM-YYYY')
  }

  render() {
    
    var projectCols = this.state.projects.map((project) =>  
      <Col span={12} key={project.id}>
        <ProjectCol span={12} project={project} getDateFormat={this.getDateFormat} />
      </Col>
    )

    return (
      <div>
       <h2>Projects</h2>
        <Row>
          <Col className='marginTB20'>
            <Button type='primary'><Link to='/projects/create'>Create</Link></Button>
          </Col>
        </Row>
        <Row gutter={16}>
          {projectCols}
        </Row>
      </div>
    )
  }
}


export default ProjectList
