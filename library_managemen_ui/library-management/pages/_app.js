import '../styles/globals.css'
import { ContextState } from '../context/ContextState';
import { WagmiConfig, createConfig, configureChains, mainnet, sepolia, createClient } from 'wagmi'
import { publicProvider } from 'wagmi/providers/public'

const { chains, publicClient, webSocketPublicClient } = configureChains(
  [sepolia],
  [publicProvider()],
)

const config = createConfig({
  autoConnect: true,
  publicClient,
  webSocketPublicClient
})

// const client = createClient()=> {
  
// }


function MyApp({ Component, pageProps }) {

  return (
    <WagmiConfig config={config}>
      <ContextState>
        <Component {...pageProps} />
      </ContextState >
    </WagmiConfig>
  )
}

export default MyApp
