import { useEffect, useState } from 'react'

interface GiphyImageProps {
  name: string
}

const GIPHY_API_KEY = import.meta.env.VITE_GIPHY_API_KEY
const FALLBACK_GIF = 'https://media.giphy.com/media/YaOxRsmrv9IeA/giphy.gif'

const GiphyImage = ({ name }: GiphyImageProps) => {
  // Sin API key configurada se muestra directamente la imagen por defecto
  const [giphyUrl, setGiphyUrl] = useState<string>(GIPHY_API_KEY ? '' : FALLBACK_GIF)

  useEffect(() => {
    if (!GIPHY_API_KEY) return

    const params = new URLSearchParams({ api_key: GIPHY_API_KEY, limit: '1', q: name })
    let cancelled = false

    fetch(`https://api.giphy.com/v1/gifs/search?${params}`)
      .then((response) => response.json())
      .then((response) => {
        if (cancelled) return
        // cuando no se encuentran imágenes se usa una por defecto
        setGiphyUrl(response.data?.length > 0 ? response.data[0].images.original.url : FALLBACK_GIF)
      })
      .catch(() => !cancelled && setGiphyUrl(FALLBACK_GIF))

    return () => {
      cancelled = true
    }
  }, [name])

  if (!giphyUrl) {
    return <p className="muted">Cargando imagen...</p>
  }

  return <img className="car-gif" src={giphyUrl} alt={name} loading="lazy" />
}

export default GiphyImage
