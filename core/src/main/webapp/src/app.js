import './global-imports'
import { el, text, mount, setChildren } from "redom";
import { App } from "redom-app";
import { PatientList } from "./view/patient-list";
import { DoctorList } from "./view/doctor-list";
import { PatientDetails } from "./view/patient-details";
import { Signin } from "./view/signin";
import { Home } from "./view/home";
import { AdminHome } from "./view/admin";
import {PatientQuestions} from "./view/patient-questions";


class AuthenticationMiddleware {
  constructor() {}
  //this is what the auth function needs to look like
  exec(currentView, nextView, params) {
    if (sessionStorage.getItem("auth") != "true") {
      return "signin";
    } else {
      return nextView;
    }
  }
}

class AuthorizationMiddleware {
  constructor() {}

  exec(currentView, nextView, params) {

    if (nextView == 'signin') {
      return nextView
    } else if (nextView == 'admin' || nextView == 'doctors') {
      if (sessionStorage.getItem("role") == 'ROLE_ADMIN') {
        return nextView
      } else {
        alert('You do not have the correct authentication!')
        return currentView
      }
    } else if (nextView == 'home' || nextView == 'patients' || nextView == 'patient') {
      if (sessionStorage.getItem("role") == 'ROLE_DOCTOR') {
        return nextView
      } else {
        alert('You do not have the correct authentication!')
        return currentView
      }
    }
  }

}

const container = el('div.container-fluid')
mount(document.body, container)
const app = new App()
        .routes({
          home: Home,
          default: Signin,
          patients: PatientList,
          patient: PatientDetails,
          admin: AdminHome,
          doctors: DoctorList,
          ask: PatientQuestions
        })
        .middlewares([new AuthenticationMiddleware(), new AuthorizationMiddleware()])
        .start(container);
