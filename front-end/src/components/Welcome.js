import React, {Component} from 'react';
import PropTypes from 'prop-types';
import * as API from '../api/API';

class Welcome extends Component {

    static propTypes = {
        username: PropTypes.string.isRequired,
        handleLogout: PropTypes.func.isRequired
    };

    state = {
        username : '',
        path:'',
        allfiles:[]
    };

    handleDirSubmit = (userdata) => {
        console.log(userdata);
        API.ListDir(userdata)
            .then((res) => {
               if (res) {
                   console.log("Now calling get")
                    API.getFiles()
                        .then((data) => {
                            console.log("data is :"+JSON.stringify(data));
                            this.setState({
                                allfiles: data,
                            });
                        });
                } else if (res.status === 401) {
                    this.setState({
                        isLoggedIn: false,
                        message: "Wrong username or password. Try again..!!"
                    });
                }
            });
    };


    componentWillMount(){
        this.setState({
            username : this.props.username
        });
    }

    componentDidMount(){
        document.title = `Welcome, ${this.state.username} !!`;
    }

    render(){
        return(
            <div className="row justify-content-md-center">
                <div className="col-md-4">
                    <div className="alert alert-warning" role="alert">
                        {this.state.username}, welcome to my App..!!
                    </div>
                    <form>
                        <div className="form-group">
                            <h1>Enter Path:</h1>
                        </div>
                        <div className="form-group">
                            <input
                                className="form-control"
                                type="text"
                                label="Path"
                                placeholder="Enter File Path"
                                value={this.state.path}
                                onChange={(event) => {
                                    this.setState({
                                        path: event.target.value
                                    });
                                }}
                            />
                        </div>
                    </form>
                    <div>
                        <h4>Files:</h4>
                        {this.state.allfiles.map((task, i) =>
                            <table className="" key={i}>
                                <tr>
                                    <td>{i+"-"}</td>
                                    <td>{task}</td>
                                </tr>
                            </table>
                        )}
                        <br /><br />
                    </div>
                   {/* <div>
                        <h4>Files in :{this.state.path} are : </h4>
                        {this.state.allfiles}
                    </div>*/}
                    <div className="form-group">
                        <button
                            className="btn btn-primary"
                            type="button"
                            onClick={() => this.handleDirSubmit(this.state)}>
                            Submit
                        </button>
                    </div>
                    <button
                        className="btn btn-danger"
                        type="button"
                        onClick={() => this.props.handleLogout(this.state)}>
                        Logout
                    </button>
                </div>
            </div>
        )
    }
}

export default Welcome;