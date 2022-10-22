import * as api from "@/api/generated";
import {useAuth} from "@/stores/auth";

const config = new api.Configuration({
  basePath: "https://junction.kuzznya.space",
  accessToken: () => {
    const auth = useAuth()
    if (!auth.isAuthenticated || auth.token == null)
      throw new Error("User is not authenticated")
    return auth.token
  },

})

export default {
  UserControllerApi: new api.UserControllerApi(config),
  AuthControllerApi: new api.AuthControllerApi(new api.Configuration({ basePath: config.basePath })),
  CourseControllerApi: new api.CourseControllerApi(config),
  GroupControllerApi: new api.GroupControllerApi(config),
  TeamControllerApi: new api.TeamControllerApi(config),
  StageControllerApi: new api.StageControllerApi(config),
  TaskControllerApi: new api.TaskControllerApi(config),
  CheckpointControllerApi: new api.CheckpointControllerApi(config),
  BattleControllerApi: new api.BattleControllerApi(config),
  CollabControllerApi: new api.CollabControllerApi(config),

  AdminUserControllerApi: new api.AdminUserControllerApi(config),
  AdminCourseControllerApi: new api.AdminCourseControllerApi(config),
  AdminGroupControllerApi: new api.AdminGroupControllerApi(config),
  AdminTeamControllerApi: new api.AdminTeamControllerApi(config),
  AdminStageControllerApi: new api.AdminStageControllerApi(config),
  AdminTaskControllerApi: new api.AdminTaskControllerApi(config),
  AdminCheckpointControllerApi: new api.AdminCheckpointControllerApi(config),
  AdminBattleControllerApi: new api.AdminBattleControllerApi(config),
  AdminCollabControllerApi: new api.AdminCollabControllerApi(config),
}
