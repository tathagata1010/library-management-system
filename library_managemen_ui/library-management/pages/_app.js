import '../styles/globals.css'
import { ContextState } from '../context/ContextState';
function MyApp({ Component, pageProps }) {

  return (
    <ContextState>
      <Component {...pageProps} />
    </ContextState >
  )
}

export default MyApp
