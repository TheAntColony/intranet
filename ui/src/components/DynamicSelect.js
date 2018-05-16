import React, {Component} from 'react'
import { Select } from 'antd'

const Option = Select.Option

class DynamicSelect extends Component {
  
  render() {
    var categorySelect = null
    if(this.props.data.length > 0) {
      const options = this.props.data.map((ele) =>
        <Option key={ele.id} value={ele.id}>{ele.name}</Option>
      );

      categorySelect = 
        <Select defaultValue={this.props.data[0].id} 
          style={{ width: 120 }} onChange={this.props.handleChange}>
          {options}
        </Select>
    }

    return (
      <div>
        {categorySelect}
      </div>)
  }
}

export default DynamicSelect
