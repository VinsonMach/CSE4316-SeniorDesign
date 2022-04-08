// from dgreif/ring with minor changes
/* eslint-disable no-console */
import { RingRestClient } from './rest-client'
import { requestInput } from './util'
import { AuthTokenResponse } from './ring-types'

export async function ringLogin(email, password) {
    const restClient = new RingRestClient({ email, password });
    return restClient;
}

// em = email, ow = password, c = authentication code
export async function haveCode(em, pw, c) {
  const email = em,
    password = pw,
    restClient = new RingRestClient({ email, password }),
    getAuthWith2fa = async (): Promise<any> => {
      const code = c
      try {
        return await restClient.getAuth(code)
      } catch (_) {
        //console.log('Incorrect 2fa code. Please try again.')
        return;
      }
    },
    auth: AuthTokenResponse = await restClient.getCurrentAuth().catch((e) => {
      if (restClient.promptFor2fa) {
        console.log(restClient.promptFor2fa)
        return getAuthWith2fa()
      }

      console.error(e)
      process.exit(1)
    })

  return auth.refresh_token
}

process.on('unhandledRejection', () => {})
